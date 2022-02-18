package util;

import connection.ConnectionTest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 封装操作数据库的工具类
 */
public class JDBCUtils {
    /**
     * 获取数据库连接
     * @return 数据库的连接
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
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
        System.out.println("connection: "+ conn );
        return conn;
    }

    /**
     * 关闭资源
     * @param conn
     * @param ps
     */
    public static void closeResource(Connection conn, Statement ps){
        //5.关闭资源
        try {
            if(ps!=null){
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn!=null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
