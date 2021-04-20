/*
 * @Classname batchTest
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

public class batchTest {

    @Test
    public void largeDataTest() {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();//计时开始
            conn = JDBCUtil.getConnections();
            String sql = "insert into goods (name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 2000; i++) {
                ps.setString(1, "test_" + i);
                ps.executeUpdate();
            }
            long end = System.currentTimeMillis();//计时结束
            System.out.println("花费时间" + (end - start));//7056
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn, ps);
        }

    }

    @Test
    public void batch1Test(){

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtil.getConnections();
            String sql = "insert into goods (name) values (?)";
            ps = conn.prepareStatement(sql);

            for (int i = 1; i<2000;i++){
                ps.setString(1,"name_i");
                ps.addBatch();//攒SQL到500条一起执行
                if(i % 500 == 0){
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));//887
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,ps);
        }


    }

}
