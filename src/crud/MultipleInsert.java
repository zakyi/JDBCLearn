package crud;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 批量增
 */



public class MultipleInsert {

    public static void update(){//可变数量的参数
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            //1.获取连接
            conn = util.JDBCUtils.getConnection();
            //2.预编译SQL语句, 返回Pre实例

            String sql = "insert into goods(name)values(?)";

            ps = conn.prepareStatement(sql);

            for(int j=1;j<=20000;j++){
                //3.填充站位符
                ps.setObject(1, "name_"+j);

                //将查询语句添加到batch中,每500次执行一次sql语句
                ps.addBatch();
                if(j%500==0){
                    ps.executeBatch();

                    ps.clearBatch();
                }

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

    public static void main(String[] args) {

        update();
    }

}
