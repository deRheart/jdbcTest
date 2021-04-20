/**
 * @classname ConnectionTest
 * @author QishengBao
 * @create 2021/4/1
 * @description 获取数据库连接
 */
package connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {

    //方式1
    @Test
    public void testConnection1() throws SQLException {
        //获取driver的实现类,存在第三方库
        Driver drive = new com.mysql.cj.jdbc.Driver();

        String url = "jdbc:mysql://localhost:3306/test";
        //用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "baoqisheng");


        Connection conn = drive.connect(url,info);
        System.out.println(conn);
    }

    //方式2, 不出现第三方api,把第三方api包装在反射中,移植性更好
    @Test
    public void testConnection2() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {

        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //java9不推荐使用newInstance(),java8中仍然推荐使用
        Driver driver =(Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test";
        //用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "baoqisheng");


        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }

    //方式3, DriverManager
    @Test
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //java9不推荐使用newInstance(),java8中仍然推荐使用
        Driver driver =(Driver) clazz.newInstance();

        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "baoqisheng";

        DriverManager.registerDriver(driver);
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }

    //方式4, 自动加载驱动
    @Test
    public void testConnection4(){
        //加载Driver时,类中的静态方法块自动注册, 可以省略手动注册代码
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/test";
            String user = "root";
            String password = "baoqisheng";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    //方式5, 通过配置文件读取
    @Test
    public void getConnections(){
        try {
            //读取配置文件的信息
            InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            Class.forName(driverClass);

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(conn);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
