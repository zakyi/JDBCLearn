package crud;

import data.Customer;
import data.Order;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementSelectTest {

    //<T>指明为泛型方法, List<T>指明返回值, Class<T> 指明返回值为不同类(反射)
    //返回多条记录
    public static <T> List<T> getInstanceList(Class<T> clazz, String sql, Object ...args){
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
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            //while有结果则进入
            //一次循环是一行结果
            while(resultSet.next()){
                //最终返回的对象:反射
                T t = clazz.newInstance();

                //获取结果集的元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //获取结果集的列数
                int columnCount = metaData.getColumnCount();

                //给t对象指定的属性赋值
                for(int i=0; i< columnCount;i++){
                    //获取某一列的值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取某一列的名字,不推荐使用
                    String columnName = metaData.getColumnName(i+1);
                    //获取某一列的别名
                    String columnLabel = metaData.getColumnLabel(i+1);

//                    System.out.println(columnValue);
                    //通过反射将一列数据装入Customer类中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                //添加到对象列表中
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            //关闭连接
            util.JDBCUtils.closeResource(conn,ps);
        }
        return null;
    }


    //<T>指明为泛型方法, T指明返回值, Class<T> 指明返回值为不同类(反射)
    //返回一条记录
    public static <T> T getInstance(Class<T> clazz, String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps= null;
        try {
            //1.获取连接
            conn = util.JDBCUtils.getConnection();
            //2.预编译SQL语句, 返回Pre实例
            ps = conn.prepareStatement(sql);

            //3.填充站位符,没有参数就不填充
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
                    Field field = clazz.getDeclaredField(columnLabel);
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

    public static void testGetInstance(){
        //order为关键字, 需要加反引号, 用来转义
        //添加变量别名
        String sql = "select order_id orderID,order_name orderName,order_date orderDate from `order` where order_id = ?";//?为占位符
        Order order = getInstance(Order.class, sql, 1);
        System.out.println(order);
        String sqlCustomer = "select id,name,email from customers where id = ?";//?为占位符
        Customer customer = getInstance(Customer.class, sqlCustomer, 1);
        System.out.println(customer);
    }

    public static void testGetInstanceList(){
        //order为关键字, 需要加反引号, 用来转义
        //添加变量别名
        String sql = "select order_id orderID,order_name orderName,order_date orderDate from `order` where order_id < ?";//?为占位符
        List<Order> list = getInstanceList(Order.class, sql, 3);
        list.forEach(System.out::println);


        String sqlCust = "select id,name,email from customers ";//?为占位符
        List<Customer> customers = getInstanceList(Customer.class, sqlCust);
        customers.forEach(System.out::println);
    }


    public static void main(String[] args) {
//        testGetInstance();
        testGetInstanceList();
    }

}
