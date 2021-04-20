/*
 * @Classname utilsTest
 * @Description 测试工具类使用
 * @Author QishengBao
 * @Date 2021/4/19
 */

package crud;

import bean.Customer;
import org.junit.Test;
import util.JDBCUtil;

import java.util.List;


public class utilTest {

    @Test
    public void testUtil() {
        String sql1 = "update `customers` set email=? where name = ?";
        JDBCUtil testUpdate = new JDBCUtil();
        testUpdate.update(sql1,"297896559@qq.com","测试1");

        String sql2 = "select id,name,birth,email from customers where id=?";
        Customer customer = new Customer();
        //第一个参数为customer的class实例,返回为结果为一条查询记录,对应一个customer对象
        Customer customer1 = testUpdate.query(customer.getClass(), sql2, 21);
        System.out.println(customer1);
    }

}
