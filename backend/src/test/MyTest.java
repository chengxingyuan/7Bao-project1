import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 340082 on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-backend-context.xml"})
@ActiveProfiles("development")
public class MyTest {
    @Test
    public void test01(){
        System.out.println("111");
    }

}
