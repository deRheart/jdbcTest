/*
 * @Classname testDbutils
 * @Description 测试apache下的DB utils工具类
 * @Author QishengBao
 * @Date 2021/4/20
 */

package crud;

import bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;
import util.JDBCUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class testDbutils {
    @Test
    public void testUpdate() throws SQLException, IOException, ClassNotFoundException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtil.getConnections();
        String sql1 = "update `customers` set email=? where name = ?";
        int count = runner.update(conn, sql1, "test1@qq.com", "测试1");
        System.out.println("修改了"+count+"条");
        JDBCUtil.closeResource(conn,null);
    }

    @Test
    public void testQueryOne() throws SQLException, IOException, ClassNotFoundException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtil.getConnections();
        String sql2 = "select id,name,birth,email from customers where id=?";

        BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql2, handler, 21);
        System.out.println(customer);

        BeanListHandler<Customer> customerListHandler = new BeanListHandler<>(Customer.class);
        String sql3 = "select id,name,birth,email from customers where id>?";
        List<Customer> list = runner.query(conn, sql3, customerListHandler, 19);
        list.forEach(System.out::println);

        JDBCUtil.closeResource(conn,null);

    }

}
