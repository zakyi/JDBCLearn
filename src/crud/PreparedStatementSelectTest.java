package crud;

import data.Customer;
import data.Order;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class PreparedStatementSelectTest {

    //<T>指明为泛型方法, T指明返回值, Class<T> 指明返回值为不同类(类似于Object)
    public static <T> T getInstance(Class<T> clazz, String sql, Object ...args){
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

            //4.执行sql,并返回结果集
            ResultSet resultSet = ps.executeQuery();

            //如果有结果则进入
            if(resultSet.next()){
                //最终返回的对象:反射
                T t = clazz.newInstance();

                //获取结果集的元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //获取结果集的列数
                int columnCount = metaData.getColumnCount();

                for(int i=0; i< columnCount;i++){
                    //获取某一列的值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取某一列的名字,不推荐使用
                    String columnName = metaData.getColumnName(i+1);
                    //获取某一列的别名
                    String columnLabel = metaData.getColumnLabel(i+1);

//                    System.out.println(columnValue);
                    //通过反射将一列数据装入Customer类中
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }
        return null;
    }
    public static void main(String[] args) {
        //order为关键字, 需要加反引号, 用来转义
        //添加变量别名
        String sql = "select order_id orderID,order_name orderName,order_date orderDate from `order` where order_id = ?";//?为占位符
        Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
    }

}
