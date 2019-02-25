package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("api/user/add")
    public Long addUser(@RequestBody User user) {
        return userDao.save(user).getUser_id();
    }
}
