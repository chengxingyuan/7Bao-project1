import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.utils.MD5;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.ISevenBaoAccountDBDAO;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by 340082 on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class MyTest {
    @Autowired
    ISevenBaoAccountManager sevenBaoAccountManager;
    @Autowired
    ISevenBaoAccountDBDAO sevenBaoAccountDBDAO;

    @Test
    public void test01() {
        System.out.println("111");
    }


    /**
     * 测试接口,
     */
    @Test
    public void test2() {
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = new SevenBaoAccountInfoEO();
        sevenBaoAccountInfoEO.setLoginAccount("123456");
        sevenBaoAccountInfoEO.setQq(456789L);
        sevenBaoAccountInfoEO.setName("22222");
        sevenBaoAccountInfoEO.setIsUserBind(true);
        sevenBaoAccountInfoEO.setUid("1111111111111111111111");
        sevenBaoAccountInfoEO.setPhoneNumber("16435689");
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO1 = sevenBaoAccountDBDAO.selectAccount("123456");
        if (null == sevenBaoAccountInfoEO1) {
//            String xx = UUID.randomUUID().toString().replaceAll("-", "");
//            String s = "ZB" + xx;
//            sevenBaoAccountInfoEO.setZbaoLoginAccount(s);
            sevenBaoAccountManager.insertSevenBao(sevenBaoAccountInfoEO);
        } else {
            sevenBaoAccountInfoEO1.setIsUserBind(false);
            sevenBaoAccountManager.updateBind(sevenBaoAccountInfoEO1);
        }

    }

    @Test
    public void test3() {
        String sign = MD5.sign("100" + "supmj7", ServicesContants.GOLD_BEAN_KEY, "utf-8");
        System.out.println(sign);
    }


}
