package rentdeck;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class HelloController {

    @GetMapping("/*")
    public String index() {
        return "Hello from spring boot using Vuee!";
    }

}
