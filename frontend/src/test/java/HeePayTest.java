import com.wzitech.Z7Bao.frontend.business.IHeePayManager;
import com.wzitech.Z7Bao.frontend.dao.redis.IZbaoPayOrderRedisDao;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by guotx on 2017/9/22 17:58.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class HeePayTest {
    @Autowired
    IHeePayManager heePayManager;
    @Autowired
    IZbaoPayOrderRedisDao redisDao;

    @Test
    public void testHeePay(){
        String tradeNo="Z7BCZ1708310000038";
        String title="汇付测试支付";
        String url = heePayManager.getPayUrl(tradeNo, title, 1, ZBaoPayType.weixinPay);
        System.out.println(url);
    }

    @Test
    public void testQuery(){
        String tradeNo = "7bao_test_0924001";
        boolean result=heePayManager.heePayQuery(tradeNo);
        System.out.println(result);
    }

}
