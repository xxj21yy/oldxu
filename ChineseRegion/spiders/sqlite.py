#!coding=utf8
import sqlite3
import threading

lock = threading.RLock()
def insert(id,name,type):
    lock.acquire()
    try:
        conn = sqlite3.connect('D:\workspace\pythonwork\github\ChineseRegion-master\ChineseRegion\spiders\example.db',
                               timeout=10)
        c = conn.cursor()
        c.execute(
            '''INSERT INTO city_code (id,name,type) VALUES ("%s","%s","%s")'''%(int(id),name,type))
        conn.commit()
        c.close()
    except Exception ,e:
        print e
    finally:
        lock.release()
def insert1(id,name,pid,type,pname):
    lock.acquire()
    try:
        conn = sqlite3.connect('D:\workspace\pythonwork\github\ChineseRegion-master\ChineseRegion\spiders\example.db',
                               timeout=10)
        c = conn.cursor()
        c.execute(
            '''INSERT INTO city_code (id,name,pid,type,pname) VALUES ("%s","%s","%s","%s","%s")''' % (
            int(id), name, int(pid), int(type), pname))
        conn.commit()
        c.close()
    except Exception ,e:
        print e
    finally:
        lock.release()

# c.execute("INSERT INTO stocks (id,name,pid,type,pname) VALUES (341802110001,'龙泉社区居委会',341802110,121,'安徽省/宣城市/宣州区/水东镇')")
# # "id"  INTEGER,
# # "name"  TEXT,
# # "pid"  INTEGER,
# # "type"  INTEGER,
# # "pname"  TEXT
# # Save (commit) the changes