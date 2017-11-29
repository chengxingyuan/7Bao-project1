import com.wzitech.Z7Bao.frontend.business.IZbaoCityManager;
import com.wzitech.Z7Bao.frontend.business.IZbaoWithdrawalsManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoCityEO;
import com.wzitech.Z7Bao.frontend.service.IBankManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Created by 340032 on 2017/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class CityTest {

    @Autowired
    IZbaoWithdrawalsManager zbaoWithdrawalsManager;

    @Autowired
    IZbaoCityManager zbaoCityManager;

    @Autowired
    ZbaoCityEO zbaoCityEO;

    @Autowired
    IBankManager bankManager;
    @Test
    public void test1(){


        bankManager.bankData();
//        IBankReponse reponse=new IBankReponse();
        //查询用户信息
//        String userLoginAccount = CurrentUserContext.getUserLoginAccount();
//        ZbaoWithdrawals zbaoWithdrawals=zbaoWithdrawalsManager.queryloginAccountNotLike("supmj10");
//        ZbaoCityEO zbaoCityEO=zbaoCityManager.selectProvinceId(Long.valueOf(zbaoWithdrawals.getProvince()));
//        ZbaoCityEO zbaoCityEO1=zbaoCityManager.selectCityId(Long.valueOf(zbaoWithdrawals.getCity()));

//
//        if (zbaoWithdrawals!=null) {
//            reponse.setBankName(zbaoWithdrawals.getBankName());
//            reponse.setRealName(zbaoWithdrawals.getRealName());
//            reponse.setProvince(zbaoCityEO.getProvinceName());
//            reponse.setCity(zbaoCityEO1.getCityName());
//
//        }

    }
}
