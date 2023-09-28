from bs4 import BeautifulSoup
import requests
import mysql.connector
import datetime as dt
import time
import uuid
from datetime import datetime, date
from multiprocessing import Pool
from fake_useragent import UserAgent
import os

AWS_RDS_HOST = os.environ.get("AWS_RDS_HOST")
AWS_RDS_PASSWORD = os.environ.get("AWS_RDS_PASSWORD")
AWS_RDS_USER = os.environ.get("AWS_RDS_USER")
AWS_RDS_DB = os.environ.get("AWS_RDS_DB")

conn = mysql.connector.connect(
    host = AWS_RDS_HOST,
    user = AWS_RDS_USER,
    password = AWS_RDS_PASSWORD,
    database = AWS_RDS_DB,
)

cursor = conn.cursor(prepared=True)

def parsing(value):
    
    brand = value[0]
    url = value[1]
    id = value[2]
    
    url = "http://www.okmall.com" + url
    print(f"{brand} 가격 업데이트 시작")
    product = []
    price = []
    update = []
    while url:
        headers = {
            'User-Aagent': UserAgent().random
        }
        for _ in range(5):
            try:
                response = requests.get(url, headers=headers)
                response.raise_for_status()
                break
            except requests.exceptions.ConnectionError as e:
                print(" 타임아웃 에러 발생")
                time.sleep(15)
                

        soup=BeautifulSoup(response.content, "html.parser")
        html = soup.select("[data-productno]")

        today_date = date.today().strftime('%Y-%m-%d')
        for a in html:
            try:
                scratch = a.find("div",class_="link-all-scratch").text
            except:
                priceId = (uuid.uuid4().hex)[:16]
                productAddress = a.find('a').get('href')
                productNum = a.get("data-productno")
                productName = a.find("span", class_="prName_PrName").text.replace("'","")

                productPrice = int(a.find("span", class_="okmall_price").text.strip('~').replace(",",""))
                productImg = a.find('img').get('data-original')
                if productImg == None:
                    productImg = productImg = a.find('img').get('src')
            
                
                product.append([productNum,str(id),productName,productImg,productAddress,today_date,today_date])
                price.append([priceId,productNum,productPrice,today_date])

                  

        url = soup.find('a',class_="nextPage")
        url = url.get("href")
        # print(f"url : {url}")
        
        if url=="":
            url = soup.find('a',class_="button_more")
            
            try:
                url = url.get("href")
            except:
                url = ""
    
    print(f"{brand} 가격 업데이트 종료")
    return product, price


def crawling():
    homepage = "https://www.okmall.com/"
    response = requests.get(homepage)
    soup=BeautifulSoup(response.content, 'html.parser')

    driver = webdriver.Chrome('C:/Users/Yun/Documents/python/okmall/chromedriver')
    driver.get(homepage)
    driver.implicitly_wait(10)

    best100 = driver.find_element(By.XPATH,'//a[@onclick="return false;" and @href="#gnb_menu10"]')
    best100.click()
    best100 = driver.find_element(By.XPATH,'//*[@id="gnb_allBrand_area"]/div')
    time.sleep(3)
    url = driver.page_source



    soup = BeautifulSoup(url, 'html.parser')
    div_element = soup.find("div",class_='best150_wrap brand_tab on')
    tags = div_element.find_all('a')
    hrefs = [a.get('href') for a in tags]
    hrefs = hrefs[:-1]

    names = [a.text for a in tags]
    names = names[:-1]
    names = [name.split(".")[1].replace(" ","").strip() for name in names]

    for i in range(len(hrefs)):
        selectBrandSql = f'''
            select * from brands where name = %s
        '''
        cursor.execute(selectBrandSql, (names[i],))
        result = cursor.fetchall()
        if result:
            continue
        else:
            insertBrandSql = f'''
                insert into brand values ('{names[i]}','{hrefs[i]}')
            '''
            cursor.execute(insertBrandSql)
            conn.commit()
            print('데이터 입력 완료')

if __name__=='__main__':

    selectQuery = '''
        select * from brands
    '''

    cursor.execute(selectQuery)
    rows = cursor.fetchall()

    brands = ['CP컴퍼니','겐조','골든구스','꼼데가르송','나이젤카본','나이키','뉴발란스','니들스','단톤',
            '디스퀘어드2','띠어리','라코스테','르메르','마르니','메종마르지엘라','메종키츠네',
            '몽클레르','무스너클','미우미우','발렌시아가','발렌티노','버버리','베르사체','보테가베네타','부테로','비비안웨스트우드',
            '살로몬','생로랑','세인트제임스','셀린느','스톤아일랜드','스튜디오니콜슨','아디다스','아미','아크네스튜디오','아크테릭스',
            '아페쎄','알렉산더맥퀸','언더아머','엔지니어드가먼츠','엠에스지엠','오프화이트','옴므플리세이세이미야케','이자벨마랑',
            '자크뮈스','지방시','칼하트WIP','커먼프로젝트','텐씨','토리버치','토즈','톰브라운','파라부트','파라점퍼스',
            '파타고니아','페라가모','펜디','폴로랄프로렌','폴스미스','프라다','하울린','호카오네오네',
            '아워레가시','남이서팔','아나토미카','오라리','클락스','안데르센안데르센','버켄스탁']

    # pool = Pool(processes=1)
    # pool.map(print, rows)   
    _product_list=[]
    _price_list=[]

    for row in rows:
        product, price = parsing(row) 
        _product_list.append(product)
        _price_list.append(price)

    if conn.is_connected():
        print("연결되었습니다")

        conn.ping(reconnect=True)
        print("연결상태 확인 완료")
    
    product_list = [item for sublist in _product_list for item in sublist]
    price_list = [item for sublist in _price_list for item in sublist]
    
    insert_price_query = '''
        insert ignore into price (price_id,product_num,product_price,create_date) values (%s,%s,%s,%s);
    '''
    for i in price_list:
        cursor.execute(insert_price_query,i)    
    
    print("price list 업데이트 완료")
    
    
    insert_product_query = '''
                    insert into products (product_num, id, product_name, product_img, product_address, recently_date) values (%s,%s,%s,%s,%s,%s)
                    ON duplicate KEY UPDATE recently_date=%s;
                    '''
    for i in product_list:
        cursor.execute(insert_product_query, i)
    
    print("product list 업데이트 완료")
       
    conn.commit()

    update_product_query = '''                    
                UPDATE products AS p
                JOIN (
                    SELECT 
                        p.product_num,
                        MAX(CASE WHEN p.rank = 1 THEN p.price_id END) AS today_price,
                        MAX(CASE WHEN p.rank = 2 THEN p.price_id END) AS yesterday_price,
                        MIN(p.price_id) AS lowest_price
                    FROM (
                        SELECT 
                            price.product_num,
                            price.price_id,
                            price.product_price,
                            ROW_NUMBER() OVER (PARTITION BY price.product_num ORDER BY price.create_date DESC) AS rank
                        FROM price
                    ) AS p
                    GROUP BY p.product_num
                ) AS subquery ON p.product_num = subquery.product_num
                SET
                    p.today_price = subquery.today_price,
                    p.yesterday_price = subquery.yesterday_price,
                    p.lowest_price = subquery.lowest_price;
                '''
    cursor.execute(update_product_query)

    print("최저가 list 업데이트 완료")

    delete_old_price_query = '''
    delete from price where product_num in (select product_num from products where recently_date < DATE_SUB(NOW(), INTERVAL 2 MONTH));
    '''
    cursor.execute(delete_old_price_query)
    
    delete_old_products_query = '''
    delete from products where recently_date < DATE_SUB(NOW(), INTERVAL 2 MONTH); 
    '''
    cursor.execute(delete_old_products_query)
    conn.commit()
