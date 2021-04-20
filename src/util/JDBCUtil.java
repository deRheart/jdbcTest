/*
 * @Classname JDBCUtil
 * @Description JDBC工具类
 * @Author QishengBao
 * @Date 2021/4/1
 */

package util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {

    public static Connection getConnections() throws IOException, ClassNotFoundException, SQLException {
        
        //加载配置文件信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        //读取prop配置信息
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String driverClass = pros.getProperty("driverClass");
        String url = pros.getProperty("url");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        //System.out.println(conn);
        return DriverManager.getConnection(url, user, password);
        }

    public static void closeResource(Connection conn, PreparedStatement ps){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if(ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String sql, Object ... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnections();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.execute();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, ps);
        }
    }

    public <T> T query(Class<T> clazz, String sql, Object... args) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnections();//数据库连接

            ps = conn.prepareStatement(sql);//

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            if (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();//newInstance()方法已经过时
                for (int i = 1; i <= columnCount; i++) {
                    Object columnValue = rs.getObject(i);//获取列值
                    String columnLabel = rsMetaData.getColumnLabel(i);//获取列表别名

                    Field field = clazz.getDeclaredField(columnLabel);//利用反射赋值
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;//返回查询对象
            }
        } catch (IOException | ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            closeResource(conn, ps, rs);
        }
        return null;
    }

}
