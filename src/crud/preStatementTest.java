/*
 * @classname preStatementTest
 * @author QishengBao
 * @date 2021/4/1
 * @description 测试数据库CRUD操作
 */

package crud;


import bean.Customer;
import org.junit.Test;
import util.JDBCUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class preStatementTest {
    @Test
    public void preparedStatementTestQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //数据库连接
            conn = JDBCUtil.getConnections();
            //预编译sql
            String sql = "select id,name,birth,email from customers where id=?";
            ps = conn.prepareStatement(sql);
            //占位符
            ps.setInt(1, 21);

            //处理结果集
            rs = ps.executeQuery();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            if (rs.next()) {
                Customer customer = new Customer();
                for (int i = 1; i <= columnCount; i++) {
                    //列值
                    Object customerValue = rs.getObject(i);
                    //列名
                    String columnLabel = rsMetaData.getColumnLabel(i);
                    //String columnName = rsMetaData.getColumnName(i);//列表的别名
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, customerValue);
                }
                System.out.println(customer);
            }
        } catch (IOException | ClassNotFoundException | SQLException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn, ps, rs);
        }


    }

    @Test
    public void  preparedStatementTestInsert()  {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取连接
            conn = JDBCUtil.getConnections();
            //2.预编译sql语句
            //INSERT INTO `test`.`customers` (`id`,`name`,`email`,`birth`,`photo`) VALUES(1,test,test@qq.com,1981-10-11,NULL)
            String sql ="insert into customers (name,email,birth) values (?, ?, ?)";
            //String sql ="insert into set ?=?, ?=?, ?=?, ?=?, ?=?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setString(1,"包启盛");
            ps.setString(2, "baoqisheng@qq.com");
            //注意日期格式,需要把Java日期转为sql日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1994-12-13");
            ps.setDate(3, new java.sql.Date(date.getTime()));
            //4.执行语句
            ps.execute();
        } catch (IOException | ClassNotFoundException | SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            JDBCUtil.closeResource(conn, ps);
        }

    }

    @Test
    public void preparedStatementTestUpdate() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取连接
            conn = JDBCUtil.getConnections();
            //SQL语句
            String sql = "update `customers` set email=? where name = ?";
            ps = conn.prepareStatement(sql);
            //填充占位符
            ps.setString(1, "18366111876@163.com");
            ps.setString(2, "包启盛");
            //执行
            ps.execute();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn, ps);
        }


    }

}
