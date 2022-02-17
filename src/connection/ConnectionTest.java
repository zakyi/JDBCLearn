package connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    //获取数据库连接的方式1
    public static void testConnection1() throws SQLException {
        //1. 创建驱动
        Driver driver = new com.mysql.cj.jdbc.Driver();

        //2. 创建连接
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        //String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8" ;
        //3. 将用户名和密码封装在properties里
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "981213");

        Connection conn = driver.connect(url, info);
        System.out.println("connection1: "+ conn );

    }

    //方式2是对方式1的迭代,目的是在程序中不出现第三方的api,具有更好的可移植性
    public static void testConnection2() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.获取Driver实现类对象(创建驱动):使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2. 创建连接
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        //String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8" ;
        //3. 将用户名和密码封装在properties里
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "981213");
        //4. 获得链接
        Connection conn = driver.connect(url, info);
        System.out.println("connection2: "+ conn );

    }

    //方式3:使用DriverManager替换Driver
    public static void testConnection3() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //1.获取Driver实现类对象(创建驱动):使用反射
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2. 提供三种基本信息
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "981213";

        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("connection3: "+ conn );
    }

    //方式4:只加载驱动, 不需要注册驱动
    public static void testConnection4() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {


        //1. 提供三种基本信息
        String url = "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "981213";

        //2.加载驱动, 加载时会自动注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("connection4: "+ conn );
    }

    //方式5:创建配置文件
    public static void testConnection5() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
        //1. 提供基本信息
        InputStream inputStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2.加载驱动, 加载时会自动注册驱动
        Class.forName(driverClass);

        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("connection5: "+ conn );
    }

    public static void main(String[] args) {
        try {
            testConnection1();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            testConnection2();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            testConnection3();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            testConnection4();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            testConnection5();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
