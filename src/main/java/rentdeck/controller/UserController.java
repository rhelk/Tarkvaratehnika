package rentdeck.controller;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rentdeck.dao.AuthorityDao;
import rentdeck.model.Users;
import rentdeck.dao.UserDao;
import rentdeck.util.JsonWebToken;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static rentdeck.Security.SecurityConstants.*;
import static rentdeck.Security.SecurityConstants.TOKEN_PREFIX;
import static rentdeck.util.PasswordEncoder.encodePassword;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorityDao authorityDao;

    @PostMapping("api/register")
    public String addUser(@RequestBody Users users, HttpServletResponse res) {
        System.out.println(users.getPassword());
        String token = JsonWebToken.genJWT(users.getUsername());
//        System.out.println(token);
//        System.out.println(users.toString());
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        users.setPassword(encodePassword(users.getPassword()));
        String tempo = userDao.save(users).toString();

//        return userDao.save(users).toString();

        System.out.println(tempo);
        return tempo;
    }

    @GetMapping("api/user/get/{id}")
    public Users getUserById(@PathVariable Long id, Principal principal) {
        Users user = userDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorityList());
        System.out.println(user.toString());
        return user;
    }

    @GetMapping("api/users")
    public List<Users> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping("api/authTest")
    public void secureResponse() {}

}
