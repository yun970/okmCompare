import os
import mysql.connector

AWS_RDS_USER=os.environ.get("AWS_RDS_USER")
AWS_RDS_PORT=os.environ.get("AWS_RDS_PORT")
AWS_RDS_HOST=os.environ.get("AWS_RDS_HOST")
AWS_RDS_DB=os.environ.get("AWS_RDS_DB")
AWS_RDS_PASSWORD=os.environ.get("AWS_RDS_PASSWORD")
print(type(int(AWS_RDS_PORT)))

AWS_RDS_HOST = AWS_RDS_HOST[:-1]

print(AWS_RDS_HOST)
conn = mysql.connector.connect(
        host = AWS_RDS_HOST,
        user = AWS_RDS_USER,
        db = AWS_RDS_DB,
        password = AWS_RDS_PASSWORD,
        )

cursor = conn.cursor()

print("asdf")
