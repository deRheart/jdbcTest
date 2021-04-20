/*
 * @Classname testJDBCTransaction
 * @Description
 * @Author QishengBao
 * @Date 2021/4/19
 */

package crud;

import org.junit.Test;
import util.JDBCUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class testJDBCTransaction {

    @Test
    public void testTransaction() {
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnections();

            conn.setAutoCommit(false);
            String sql1 = "update user_table set balance = balance + 100 where user = ?";
            update(conn, sql1, "AA");

            //System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance - 100 where user = ?";
            update(conn, sql2, "BB");

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                System.out.println("异常,然后回滚");
                try {
                    conn.rollback();
                    System.out.println("成功回滚");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

    }


    //需要单独处理数据库连接
    public void update(Connection conn , String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            // 1.获取PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 3.执行sql语句
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 4.关闭资源
            JDBCUtil.closeResource(null, ps);
        }
    }
}
