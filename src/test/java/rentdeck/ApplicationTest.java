package rentdeck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
import rentdeck.controller.IntegrationTests;
import rentdeck.controller.PropertyControllerTest;
import rentdeck.controller.UserControllerTest;


@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PropertyControllerTest.class,
        UserControllerTest.class,
        IntegrationTests.class
})
public class ApplicationTest {

    @Test
    public void contextLoads() throws Exception {
    }

}