package statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;



public class PreparedStatementTest {





    /**
     * 测试插入数据库
     */
    public static void testInsert() {
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            //1.获取连接
            conn = util.JDBCUtils.getConnection();
            //2.预编译SQL语句, 返回Pre实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";//???为占位符
            ps = conn.prepareStatement(sql);

            //3.填充站位符
            ps.setString(1, "tom");
            ps.setString(2, "tom@gmail.com");
            //3.1. 进行时间的格式化
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = simpleDateFormat.parse("1998-12-13");
            ps.setDate(3, new java.sql.Date(date.getTime()));

            //4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }
    }

    /**
     * 测试修改test数据库customer表的记录
     */
    public static void testUpdate(){

        Connection conn = null;
        PreparedStatement ps= null;
        try {
            //1.获取连接
            conn = util.JDBCUtils.getConnection();
            //2.预编译SQL语句, 返回Pre实例
            String sql = "update customers set name = ? where id = ?";//???为占位符
            //生成的ps已经带有sql语句, 所以叫预编译
            ps = conn.prepareStatement(sql);

            //3.填充站位符
            ps.setString(1, "Mark");
            ps.setString(2, "18");

            //4.执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }
    }



    public static void main(String[] args) {
            //testInsert();
        testUpdate();
    }

}
