"""仅由具有数据库管理权限的管理员使用。需先以独立方式核验用户身份。"""
import argparse, getpass, os
import bcrypt, pymysql

def main():
 parser=argparse.ArgumentParser(description='查看密码找回申请并在核验身份后重置密码')
 parser.add_argument('--reset',type=int,help='已完成人工身份核验的申请编号；省略时列出待处理申请')
 args=parser.parse_args()
 password=os.environ.get('TIJIAN_DB_PASSWORD') or getpass.getpass('数据库密码：')
 conn=pymysql.connect(host='127.0.0.1',user='root',password=password,database='tijian',autocommit=False)
 try:
  with conn.cursor() as cur:
   if not args.reset:
    cur.execute("SELECT r.requestId,r.userId,u.realName,r.createdAt FROM password_reset_request r JOIN users u ON u.userId=r.userId WHERE r.state='PENDING' ORDER BY r.createdAt")
    for row in cur.fetchall(): print('申请编号：{} 账号：{} 姓名：{} 申请时间：{}'.format(*row))
    return
   cur.execute("SELECT userId FROM password_reset_request WHERE requestId=%s AND state='PENDING' FOR UPDATE",(args.reset,));row=cur.fetchone()
   if not row: raise ValueError('申请不存在或已处理')
   print('待重置账号：',row[0]);confirm=input('是否已通过独立方式核验该用户身份？输入 YES 继续：')
   if confirm!='YES':return
   new=getpass.getpass('输入新密码：');again=getpass.getpass('再次输入新密码：')
   if new!=again or not 1<=len(new)<=20 or len(new.encode('utf-8'))>72:raise ValueError('密码不一致或不符合长度要求')
   hashed=bcrypt.hashpw(new.encode('utf-8'),bcrypt.gensalt()).decode('ascii')
   cur.execute('UPDATE users SET password=%s WHERE userId=%s',(hashed,row[0]))
   cur.execute("UPDATE password_reset_request SET state='DONE',activeMarker=NULL,completedAt=NOW() WHERE requestId=%s",(args.reset,));conn.commit();print('密码已重置，申请已完成。')
 finally:conn.close()
if __name__=='__main__':main()
