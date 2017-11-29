import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.gamegold.common.enums.SwiftpassPayStatus;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import com.wzitech.Z7Bao.frontend.business.ISwiftpassPayManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;


/**
 * Created by guotx on 2017/8/21 16:25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class SwiftpassPayTest {

    @Autowired
    ISwiftpassPayManager payManager;
    @Autowired
    IFundManager fundManager;
    @Test
    public void testPay(){
        String tradeNo="7BAO_TEST_0922003";
        String body = "7宝测试";
        Integer amount = 300000;
        String url = payManager.getUrl(tradeNo, body, amount, ZBaoPayType.aliPay);
        System.out.println("URL："+url);
    }

    @Test
    public void testPayCheck(){
        String outTradeNo="Z7BCZ170824000002";
        SwiftpassPayStatus b = payManager.checkPayStatus(outTradeNo);
        System.out.println(b.getCode());
    }

    @Test
    public void testChangeAccount(){
//        String loginAccount="supmj11";
//        BigDecimal amount=new BigDecimal("-18.25");
//        boolean b = fundManager.updateGoldBeanAccount(loginAccount, amount);
//        System.out.println(b);
    }


}
