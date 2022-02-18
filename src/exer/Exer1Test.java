package exer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Exer1Test {

    /**
     * 练习1:customer表添加一条数据
     * @param args
     */


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名");
        String name = scanner.next();
        System.out.println("请输入邮箱");
        String email = scanner.next();
        System.out.println("请输入生日");
        String birthday = scanner.next();

        String sql = "insert into customers(name,email,birth)values(?,?,?)";

        int insertCount = update(sql,name,email,birthday);
        if(insertCount>0){
            System.out.println("插入成功");
        }else{
            System.out.println("插入失败");
        }
    }

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
             * ps.execute();
             * 若执行查询,有返回结果,则此方法返回true
             * 若执行增删改,没有返回结果,则此方法返回false
             */
            //ps.execute();
            /*
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
}
