package blob;

import data.Customer;
import util.JDBCUtils;

import java.io.*;
import java.sql.*;

/**
 * 测试使用PreparedStatement操作Blob类型的数据
 */

public class BlobTest {
    //向数据表customers中插入Blob类型的字段

    public static void testInsert(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("1.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";//???为占位符
        crud.TestCRUD.update(sql, "张宇豪", "zyh@gmial.com","1992-12-12",fileInputStream);

    }

    //查询Blob字段
    public static void testQuery() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select id,name,email,birth,photo from customers where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,16);

        ResultSet rs = ps.executeQuery();
        InputStream binaryStream = null;
        FileOutputStream fos = null;
        if(rs.next()){
            System.out.println("111");
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String email = rs.getString(3);
            java.sql.Date birth = rs.getDate(4);

            Customer customer = new Customer(id, name, email, birth);
            //将Blob类型的字段下载下来,以文件的方式保存在本地
            Blob photo = rs.getBlob("photo");
            binaryStream = photo.getBinaryStream();
            fos = new FileOutputStream("zhuyin.jpg");
            byte[] buffer = new byte[1024];
            int len;
            while((len = binaryStream.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
        }

        JDBCUtils.closeResource(conn, ps);
        rs.close();
        fos.close();
        binaryStream.close();


    }



    public static void main(String[] args) {
//        testInsert();
        try {
            testQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
