/*
 * @Classname preStaBlobName
 * @Description 测试操作Blob数据
 * @Author QishengBao
 * @Date 2021/4/19
 */

package crud;

import org.junit.Test;
import util.JDBCUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class preStaBlobTest {

    @Test
    public void preparedStatementTestBlob() {
        Connection conn = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try {
            conn = JDBCUtil.getConnections();

            String sql = "update `customers` set `photo`=? where name=?";

            ps = conn.prepareStatement(sql);

            fis = new FileInputStream("src/test.jpg");
            ps.setBlob(1, fis);
            ps.setString(2, "测试1");

            ps.execute();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn, ps);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}