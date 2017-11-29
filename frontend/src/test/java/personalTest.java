import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.business.ISystemConfigManager;
import com.wzitech.Z7Bao.frontend.entity.SystemConfig;
import com.wzitech.Z7Bao.frontend.service.IPersonalService;
import com.wzitech.Z7Bao.frontend.service.dto.ISevenBaoFundReponse;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.enums.ZbaoFundDetailType;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
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

import java.io.IOException;
import java.util.Date;

/**
 * Created by 340032 on 2017/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class personalTest {


    @Autowired
    IFundManager fundManager;
    @Autowired
    IPersonalService personalService;
    @Autowired
    ISystemConfigManager systemConfigManager;
    @Autowired
    ISevenBaoAccountManager sevenBaoAccountManager;

    @Test
    public void test1(){
//        personalService.qureyPersonal();
        personalService.queryNumber();
    }

    @Test
    public void test2(){
//        fundManager.creatRecharge( "US14022163863213-0300","500",null,null, "老资金");
    }

    @Test
    public void  test3() throws IOException {
        String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s", "supmj78",
                "156116", "测试啊", "true", "US12122233351001-013B", "18112345678",
                "1504856235597","serKey");
        String toEncrypt = EncryptHelper.md5(format);
        //1504850617080
        System.out.println(new Date().getTime());
        System.out.println(toEncrypt);
    }


    @Test
    public void test5(){
        ISevenBaoFundReponse reponse=new ISevenBaoFundReponse();
        SystemConfig systemConfig=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());
        SystemConfig systemConfig1=systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_REMIND_LINE.getKey());
        //保证金
        reponse.setConfigKey(systemConfig.getConfigKey());
        reponse.setConfigValue(systemConfig.getConfigValue());
        //可用余额
        reponse.setAvailableFundKey(systemConfig1.getConfigKey());
        reponse.setAvailableFundValue(systemConfig1.getConfigValue());

        System.out.println(systemConfig.getConfigValue());
        System.out.println(systemConfig1.getConfigValue());
    }


    @Test
    public void test6() throws IOException {
        String format = String.format("%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", "supmj3",
                "1231313", "狼", "true", "US12072040278001-00D0","15625632014",
                "1503908","0","1503908", "serKey");
        String toEncrypt = EncryptHelper.md5(format);
        System.out.println(toEncrypt);

    }

    @Test
    public void testUpdate(){
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = new SevenBaoAccountInfoEO();
        sevenBaoAccountInfoEO.setIsUserBind(false);
        sevenBaoAccountInfoEO.setLoginAccount("M2_14788888888");
        sevenBaoAccountInfoEO.setId(167L);
        sevenBaoAccountManager.updateBind(sevenBaoAccountInfoEO);
    }

}
