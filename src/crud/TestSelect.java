package crud;

import data.Customer;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class TestSelect {
    /**
     * 对Customer表的通用的查询操作
     */
    public static Customer selectCustomer(String sql, Object ...args){
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
                //最终返回的对象
                Customer cust = new Customer();

                //获取结果集的元数据
                ResultSetMetaData metaData = resultSet.getMetaData();
                //获取结果集的列数
                int columnCount = metaData.getColumnCount();

                for(int i=0; i< columnCount;i++){
                    //获取某一列的值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取某一列的名字
                    String columnName = metaData.getColumnName(i+1);

                    //System.out.println(columnValue);
                    //通过反射将一列数据装入Customer类中
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
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
        String sql = "select id,name,email,birth from customers where id = ? ";//?为占位符
        Customer customer = selectCustomer(sql, 13);
        System.out.println(customer);
    }

}
