package transaction;


import util.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionTest {

    /**
     * 针对于数据表user_table来说:
     * 用户AA给BB转账100
     * update user_table set balance - 100 where user ='AA'
     * update user_table set balance + 100 where user ='BB'
     */
    public static void testUpdate1(){
        String sql1 = "update user_table set balance = balance - 100 where user =?";
        update(sql1, "AA");

        String sql2 = "update user_table set balance = balance + 100 where user =?";
        update(sql2, "BB");
    }


    /**
     * 通用的增删改操作1.0
     */
    public static int update(String sql, Object ...args){//可变数量的参数
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            //1.获取连接
            conn = util.JDBCUtils.getConnection();
            //2.预编译SQL语句, 返回Pre实例
            ps = conn.prepareStatement(sql);

            //3.填充站位符
            for(int i=0; i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }

            //4.执行sql
            /*
             * 方式1:ps.execute();
             * 若执行查询,有返回结果,则此方法返回true
             * 若执行增删改,没有返回结果,则此方法返回false
             */
            //ps.execute();

            /*
            方式2:ps.executeUpdate()
            返回0,表示影响行数为0
            返回n,表示影响行数为n
             */
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }
        return 0;
    }

    /**
     * 通用的增删改操作2.0 考虑事务
     */
    public static int update2(Connection conn, String sql, Object ...args){//可变数量的参数

        PreparedStatement ps= null;
        try {

            //2.预编译SQL语句, 返回Pre实例
            ps = conn.prepareStatement(sql);

            //3.填充站位符
            for(int i=0; i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }

            //4.执行sql
            /*
             * 方式1:ps.execute();
             * 若执行查询,有返回结果,则此方法返回true
             * 若执行增删改,没有返回结果,则此方法返回false
             */
            //ps.execute();

            /*
            方式2:ps.executeUpdate()
            返回0,表示影响行数为0
            返回n,表示影响行数为n
             */
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(null,ps);
        }
        return 0;
    }
    public static void testUpdate2(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //1. 取消数据的自动提交
            conn.setAutoCommit(false);

            String sql1 = "update user_table set balance = balance - 100 where user =?";
            update2(conn,sql1, "AA");
            String sql2 = "update user_table set balance = balance + 100 where user =?";
            update2(conn, sql2, "BB");

            //2. 提交数据
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            //3. 出现异常需要回滚数据
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally{
            try {
                //将连接状态改成默认状态
                //针对使用数据库连接池时
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn,null);
        }
    }

    public static void main(String[] args) {
        testUpdate2();
    }


}
