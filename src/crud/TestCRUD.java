package crud;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestCRUD {
    /**
     * 通用的增删改操作
     */

    public static void update(String sql, Object ...args){//可变数量的参数
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
             * ps.execute();
             * 若执行查询,有返回结果,则此方法返回true
             * 若执行增删改,没有返回结果,则此方法返回false
             */
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }

    }

    /**
     * 使用通用增删改查测试删除数据库
     */
    public static void testDelete(){
        String sql = "delete from customers where id = ?";//???为占位符
        update(sql, 3);
    }

    /**
     * 使用通用增删改查测试更新数据库
     */
    public static void testUpdate(){
        //order为关键字, 需要加反引号, 用来转义
        String sql = "update `order` set order_name = ? where order_id = ?";//???为占位符
        update(sql, "DD", 2);
    }

    public static void main(String[] args) {
        testUpdate();
    }
}
