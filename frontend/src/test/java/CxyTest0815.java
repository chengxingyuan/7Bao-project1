import com.wzitech.Z7Bao.frontend.business.ITenPayManager;
import com.wzitech.gamegold.common.dao.IMainStationKeyRedisDAO;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.common.usermgmt.entity.MainStationKeyEO;
import com.wzitech.gamegold.common.usermgmt.impl.GameUserManagerImpl;
import com.wzitech.gamegold.usermgmt.business.ISevenBaoAccountManager;
import com.wzitech.gamegold.usermgmt.entity.SevenBaoAccountInfoEO;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class CxyTest0815 {
    @Autowired
    IMainStationKeyRedisDAO mainStationKeyRedisDAO;
    @Autowired
    IGameUserManager gameUserManager;
//    @Autowired
//    MainStationKeyEO mainStationKeyEO;
    @Autowired
    ITenPayManager tenPayManager;
    @Autowired
    private ISevenBaoAccountManager sevenBaoAccountManager;

    @Test
    public void testAddAmount(){
        BigDecimal fee = new BigDecimal("99999");
        SevenBaoAccountInfoEO sevenBaoAccountInfoEO = sevenBaoAccountManager.queryDataByPropWithUid("US15021233622001-0479");
        //tenPayManager.updateFeeBothInZbaoAndGameGold(fee,sevenBaoAccountInfoEO);
    }

    @Test
    public void addKeyRedisDaoTest(){
        MainStationKeyEO mainStationKeyEO = new MainStationKeyEO();
        mainStationKeyEO.setAccessToken("1633334");
        mainStationKeyEO.setAccessSecret("1613334");
        mainStationKeyRedisDAO.saveKey(mainStationKeyEO);
    }

    @Test
    public void getTest(){
        MainStationKeyEO key = mainStationKeyRedisDAO.getKey();
        System.out.println(key.toString()+"****************MMMMMMMMMMMMMMM");
    }

    @Test
    public void clearTest(){
        mainStationKeyRedisDAO.clearKey();
    }

    @Test
    public void getRequestPasswordKeyTest(){
        String s= null;
        if (StringUtils.isBlank(s)){
            System.out.println("=================================");
        }
        gameUserManager.getRequestPasswordKey();
    }
}
