package rentdeck;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
import rentdeck.controller.IntegrationRentTest;
import rentdeck.controller.IntegrationTests;
import rentdeck.controller.PropertyControllerTest;
import rentdeck.controller.RentController;
import rentdeck.controller.RentControllerTest;
import rentdeck.controller.UserControllerTest;


@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PropertyControllerTest.class,
        UserControllerTest.class,
        RentControllerTest.class,
        IntegrationTests.class,
        IntegrationRentTest.class
})
public class ApplicationTest {

    @Test
    public void contextLoads() throws Exception {
    }

}