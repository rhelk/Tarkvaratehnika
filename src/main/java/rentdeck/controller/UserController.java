package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rentdeck.model.Users;
import rentdeck.dao.UserDao;

import java.security.Principal;

import static rentdeck.util.PasswordEncoder.encodePassword;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping("api/user/add")
    public Long addUser(@RequestBody Users users) {
        users.setPassword(encodePassword(users.getPassword()));
        return userDao.save(users).getUser_id();
    }

    @GetMapping("api/user/get/{id}")
    public Users getUserById(@PathVariable Long id, Principal principal) {
        return userDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("api/secureTest")
    public String secureResponse(Principal principal) {
        return "Hello, " + principal.getName() + ". You have the token";
    }



}
