from bs4 import BeautifulSoup
import requests
import mysql.connector
import datetime as dt
from selenium import webdriver
from selenium.webdriver.common.by import By 
import time
import uuid
from datetime import datetime, date


conn = mysql.connector.connect(
    
)

cursor = conn.cursor()

def itemExtract(item): 
    item = item.strip('()').split()[0]
    return item

def renewLowestPrice():
    # 오늘의 최저가
    # select 



    # 가격 페이지에서 푸시알람 설정
    # 5개까지만 가능하게 설정
    # 알람을 설정한 날짜의 가격보다 더 낮은 가격이 갱신되었을 경우 알람 (메일 발송)
    # 스프링부트에서 설정 -> 
    pass


def parsing(brand,url,id):

    
    recently_date = date.today()
    
    url = "http://www.okmall.com" + url
    print(f"{brand} 가격 업데이트 시작")
    while url:
        headers = {
            'user-agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36'
        }
        try:
            response = requests.get(url, headers=headers)
        
        except:
            time.sleep(15)
            response = requests.get(url, headers=headers)

        soup=BeautifulSoup(response.content, "html.parser")
        html = soup.select("[data-productno]")
        time.sleep(5)

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
                
                insert_query = f'''
                    insert ignore into price (price_id,product_num,product_price,create_date) values ('{priceId}','{productNum}','{productPrice}','{recently_date}');
                '''
                cursor.execute(insert_query)

                lowestPriceQuery = f'''
                    select price_id from price where product_num = {productNum} order by product_price limit 1;
                    '''
                
                cursor.execute(lowestPriceQuery)
                _lowestPrice = (cursor.fetchall())
                print(productNum, _lowestPrice)
                try:
                    lowestPrice = _lowestPrice[0][0].decode('utf-8')
                except:
                    lowestPrice = None

                # try:
                #     lowestPriceQuery = f'''
                #     select price_id from price where product_num = {productNum} order by product_price limit 1;
                #     '''
                
                #     cursor.execute(lowestPriceQuery)
                #     lowestPrice = (cursor.fetchall())
                #     print(productNum, lowestPrice)
                #     lowestPrice = lowestPrice[0][0].decode('utf-8')

                    
                    
                    

                
                yesterdayPriceQuery = f'''
                    select price_id from price where product_num = {productNum} order by create_date desc limit 1 offset 1;
                '''            
                cursor.execute(yesterdayPriceQuery)
                yesterdayPrice = cursor.fetchall()
                print(productNum, yesterdayPrice)
                try:
                    yesterdayPrice = yesterdayPrice[0][0].decode('utf-8')
                except:
                    yesterdayPrice = None
                
                
                todyPrice = priceId

                

                if productName != None:
                    insert_query = f'''
                    insert into products (product_num, id, product_name, product_img, product_address) values ('{productNum}','{id}','{productName}','{productImg}','{productAddress}')
                    ON duplicate KEY UPDATE recently_date='{recently_date}', product_address='{productAddress}', product_img = '{productImg}';
                    '''
                    cursor.execute(insert_query)

                    update_query = f'''
                        update products
                        set today_price = '{todyPrice}',
                        yesterday_price = '{yesterdayPrice}',
                        lowest_price = '{lowestPrice}'
                        where product_num = {productNum};

                    '''
                    cursor.execute(update_query)

                # products 테이블에 todayPrice, yesterdayPrice, LowestPrice에 각각 select 한 price_id 삽입
                
                
                
                

        url = soup.find('a',class_="nextPage")
        url = url.get("href")
        print(f"url : {url}")
        
        if url=="":
            url = soup.find('a',class_="button_more")
            
            try:
                url = url.get("href")
            except:
                url = ""
    conn.commit()
    print(f"{brand} 가격 업데이트 종료")
#차후 수정 필요

#차후 수정 필요

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

# crawling()




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

for row in rows:
    if row[0] in brands:
        parsing(row[0],row[1],row[2])