#!coding=utf8
from DB_connetion_pool import getPTConnection
import threading
import logging

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
                    datefmt='%a, %d %b %Y %H:%M:%S',
                    filename='myapp.log',
                    filemode='w')
lock = threading.RLock()
def insert(id,name,type):
    lock.acquire()
    with getPTConnection() as db:
        # SQL 查询语句;
        try:
            db.cursor.execute('''INSERT INTO city_code (id,name,type) VALUES ("%s","%s","%s")''' % (int(id), name, type))
            db.conn.commit()
        except Exception, e:
            logging.warning("Error: insert error{%s}"%[e])
        finally:
            lock.release()

def insert1(id,name,pid,type,pname):
    lock.acquire()
    with getPTConnection() as db:
        # SQL 查询语句;
        try:
            db.cursor.execute('''INSERT INTO city_code (id,name,pid,type,pname) VALUES ("%s","%s","%s","%s","%s")''' % (
                    int(id), name, int(pid), int(type), pname))
            db.conn.commit()
        except Exception, e:
            logging.warning("Error: insert error{%s}"%[e])
        finally:
            lock.release()