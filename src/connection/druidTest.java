/*
 * @Classname druidTest
 * @Description
 * @Author QishengBao
 * @Date 2021/4/20
 */

package connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class druidTest {
    @Test
    public void testdruid() throws Exception {
        Properties pro = new Properties();
        pro.load(druidTest.class.getClassLoader().getResourceAsStream("druid.properties"));
        DataSource druidDataSource = DruidDataSourceFactory.createDataSource(pro);
        Connection connection = druidDataSource.getConnection();
        System.out.println(connection);


    }
}
