import com.wzitech.Z7Bao.frontend.service.IQqService;
import com.wzitech.Z7Bao.frontend.service.dto.QqServiceReponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
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
 * Created by 340032 on 2017/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class QQtest {

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IUserInfoDBDAO userInfoDBDAO;



    @Autowired
    IQqService qqService;

    @Test
    public void test1(){
//        ResponseStatus responseStatus = new ResponseStatus();
//        QqServiceReponse reponse=new QqServiceReponse();
//        reponse.setSuccess(false);
//        reponse.setException(true);
//        responseStatus.setMessage("获取失败");
//        reponse.setResponseStatus(responseStatus);
//
        qqService.queryQq();


//        UserInfoEO userInfoEO=userInfoManager.selectByQq();
//            System.out.println(userInfoEO);
//
//            if (userInfoEO!=null){
//                if (userInfoEO.getIsQqService()==false){
//                    reponse.setQq(userInfoEO.getQq());
//                    System.out.println(reponse);
//                }
//            }


    }
    }

