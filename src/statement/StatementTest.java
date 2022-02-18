package statement;

import java.util.Scanner;

//public class StatementTest {
//    public static void testLogin(){
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("请输入用户名");
//        String user = scanner.next();
//
//        System.out.println("请输入密码");
//        String password = scanner.next();
//        String sql = "SELECT user,password FROM user_table WHERE user = '"+ user + "' AND password = '"+ password + "'";
//
//        //get没有实现,返回一个USERNAME
//        //get中有statement, 会出现sql注入
//        User returnUser = get(sql,User.class);
//
//        if(returnUser != null){
//            System.out.println("登陆成功");
//        }else{
//            System.out.println("用户名不存在或密码错误");
//        }
//    }
//}
