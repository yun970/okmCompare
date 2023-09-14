from bs4 import BeautifulSoup
import requests
import mysql.connector
import datetime as dt
from selenium import webdriver
from selenium.webdriver.common.by import By 
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
    autocommit = True
)


cursor = conn.cursor()

def insert_data_sql(brand,id,priceId,productAddress,productNum,productName,productPrice,productImg):
    

    insert_query = f'''
        insert ignore into price (price_id,product_num,product_price,create_date) values ('{priceId}','{productNum}','{productPrice}','{recently_date}');
    '''
    cursor.execute(insert_query)

    if productName != None:
        insert_query = f'''
                    insert into products (product_num, id, product_name, product_img, product_address) values ('{productNum}','{id}','{productName}','{productImg}','{productAddress}')
                    ON duplicate KEY UPDATE recently_date='{recently_date}', product_address='{productAddress}', product_img = '{productImg}';
                    '''
        cursor.execute(insert_query)

        update_product_query ='''
                        UPDATE products
                        SET today_price = %s, yesterday_price = (
                            SELECT price_id
                            FROM price
                            WHERE product_num = %s
                            ORDER BY create_date DESC
                            LIMIT 1 OFFSET 1
                        ), lowest_price =(
                            SELECT price_id
                            FROM price
                            WHERE product_num = %s
                            ORDER BY product_price
                            LIMIT 1
                        )
                        WHERE product_num = %s
                    '''

        cursor.execute(update_product_query, (priceId, productNum,productNum,productNum))

def insert_data_file(brand,id,priceId,productAddress,productNum,productName,productPrice,productImg):
    
    # with open(f"{brand}.txt","w+") as f:
    #     f.write(f"{priceId},{productAddress},{productNum},{productName},{productPrice},{productImg},{id}")
    # print(f"{priceId},{productAddress},{productNum},{productName},{productPrice},{productImg},{id}")
    pass


def itemExtract(item): 
    item = item.strip('()').split()[0]
    return item

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
        for _ in range(3):
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
            
                
                product.append((productNum,str(id),productName,productImg,productAddress,today_date,today_date))
                price.append((priceId,productNum,productPrice,today_date))
                update.append((priceId,productNum,productNum,productNum))

                

                # lowestPriceQuery = f'''
                #     select price_id from price where product_num = {productNum} order by product_price limit 1;
                #     '''
                
                # cursor.execute(lowestPriceQuery)
                # _lowestPrice = (cursor.fetchall())
                # print(productNum, _lowestPrice)
                # try:
                #     lowestPrice = _lowestPrice[0][0].decode('utf-8')
                # except:
                #     lowestPrice = None
                                 
                    

                
                # yesterdayPriceQuery = f'''
                #     select price_id from price where product_num = {productNum} order by create_date desc limit 1 offset 1;
                # '''            
                # cursor.execute(yesterdayPriceQuery)
                # yesterdayPrice = cursor.fetchall()
                # print(productNum, yesterdayPrice)
                # try:
                #     yesterdayPrice = yesterdayPrice[0][0].decode('utf-8')
                # except:
                #     yesterdayPrice = None


                
                
                
                
                

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
    return product, price, update


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

def tempfunc(url):
    print("url0 : ",url[0], "url1 : ",url[1],"url2 : ", url[2])

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
    pool = Pool(processes=2)
    # pool.map(print, rows)   
    url_list=[]
    for row in rows:
        if row[0] in brands:
            # parsing(row)   
            url_list.append(row)

    result = pool.map(parsing, url_list)   
    pool.close()
    pool.join()
    _product_list, _price_list, _update_list = zip(*result)

    product_list = [item for sublist in _product_list for item in sublist]
    price_list = [item for sublist in _price_list for item in sublist]
    update_list = [item for sublist in _update_list for item in sublist]
    
    with open("product.txt","w",encoding="utf8") as f:
        for product in product_list:
            f.write("\n\n\--------------- 절취선 ---------------n\n")
            f.write(str(product))
    with open("price.txt","w",encoding="utf8") as f:
        for product in price_list:
            f.write("\n\n\--------------- 절취선 ---------------n\n")
            f.write(str(product))
    with open("update.txt","w",encoding="utf8") as f:
        for product in update_list:
            f.write("\n\n\--------------- 절취선 ---------------n\n")
            f.write(str(product))

    insert_product_query = '''
                    insert into products (product_num, id, product_name, product_img, product_address, recently_date) values (%s,%s,%s,%s,%s,%s)
                    ON duplicate KEY UPDATE recently_date=%s;
                    '''
    for i in product_list:
        print(i)
        cursor.execute(insert_product_query, i)
        print("진행중..")
    
    print("product list 업데이트 완료")
    
    insert_price_query = '''
        insert ignore into price (price_id,product_num,product_price,create_date) values (%s,%s,%s,%s);
    '''
    for i in _price_list:
        cursor.executemany(insert_price_query,i)    
        print("진행중..")
    print("price list 업데이트 완료")
    
    conn.commit()

    update_product_query ='''
                        UPDATE products
                        SET today_price = %s, yesterday_price = (
                            SELECT price_id
                            FROM price
                            WHERE product_num = %s
                            ORDER BY create_date DESC
                            LIMIT 1 OFFSET 1
                        ), lowest_price =(
                            SELECT price_id
                            FROM price
                            WHERE product_num = %s
                            ORDER BY product_price
                            LIMIT 1
                        )
                        WHERE product_num = %s
                    '''
    for i in _update_list:
        cursor.executemany(update_product_query, i)
        print("진행중..")
    
    print("최저가 list 업데이트 완료")

    conn.commit()