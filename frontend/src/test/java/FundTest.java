import com.wzitech.Z7Bao.frontend.business.IFundManager;
import com.wzitech.Z7Bao.frontend.dao.rdb.IZbaoFundDetailDBDAO;
import com.wzitech.Z7Bao.frontend.service.IFundService;
import com.wzitech.Z7Bao.frontend.service.dto.FundRequest;
import com.wzitech.Z7Bao.frontend.service.dto.FundResponse;
import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by 340082 on 2017/8/18.
 * jiyangxin
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class FundTest {
    @Autowired
    private IFundManager fundManager;

    @Autowired
    private IFundService fundService;
    @Autowired
    private IZbaoFundDetailDBDAO detailDBDAO;

    @Test
    public void testCreatRecharge(){
//        fundManager.creatRecharge("US12072359508001-00D3",new BigDecimal("100"), HandleType.addFund.getName(),"admin@5173.com");
    }

    @Test
    public void testCreatRecharge1() throws IOException {
        FundRequest fundRequest = new FundRequest();
        fundRequest.setUserId("US12072359508001-00D3");
        fundRequest.setOrderId("sdf1823719823");
        fundRequest.setMoney(new BigDecimal("100.99"));
        fundRequest.setRemark("sdfas");

        String format = String.format("%s_%s_%s_%s_%s", "4Uj3V9%", fundRequest.getUserId(), fundRequest.getOrderId(),
                fundRequest.getMoney(), fundRequest.getRemark());
        String toEncrypt = EncryptHelper.md5(format);
        fundRequest.setSign(toEncrypt);
        System.out.println(toEncrypt);
        fundService.etchRecharge(fundRequest,null);
    }

    @Test
    public void testCreatRecharge2() throws IOException {
        FundRequest fundRequest = new FundRequest();
        fundRequest.setUserId("US12072359508001-00D3");
        fundRequest.setOrderId("sdf1823719823");
        fundRequest.setMoney(new BigDecimal("100.99"));
        fundRequest.setRemark("sdfas");

        String format = String.format("%s_%s_%s_%s_%s", fundRequest.getUserId(), fundRequest.getOrderId(),
                fundRequest.getMoney(), fundRequest.getRemark(), "4Uj3V9%");
        String toEncrypt = EncryptHelper.md5(format);
        fundRequest.setSign(toEncrypt);
        fundService.payment(fundRequest,null);
    }


    @Test
    public void testCreatRecharge3() throws IOException {
        FundRequest fundRequest = new FundRequest();
        fundRequest.setUserId("US13051648929001-01FC");
        fundRequest.setOrderId("N2017082801234501668");
        fundRequest.setMoney(new BigDecimal("0.01"));
        fundRequest.setRemark("sdfas");

        String format = String.format("%s_%s_%s", "4Uj3V9%", fundRequest.getUserId(),fundRequest.getOrderId());
        System.out.println(format);
        String toEncrypt = EncryptHelper.md5(format);
        fundRequest.setSign(toEncrypt);
        System.out.println(toEncrypt);
        FundResponse fundResponse = fundService.transferState(fundRequest, null);
        System.out.println(fundResponse);
    }

    @Test
    public void testChangeDetailAmount() throws BusinessException {
//        int result=detailDBDAO.changeRechargeStatus("Z7BCZ178430230943",null,false);
//        System.out.println(result);
        fundManager.rechargeComplete("Z7BCZ1709110006146",new BigDecimal(1),"test436546543757");
    }
}
