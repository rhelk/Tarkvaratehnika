package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rentdeck.dao.RentDao;
import rentdeck.model.Rent;

import java.security.Principal;

@CrossOrigin
@RestController
public class RentController {

    @Autowired
    RentDao rentDao;

    @PostMapping("api/rent")
    public Rent getPropertyById(@RequestBody Rent rent/*, Principal principal*/) {

        System.out.println("GOT IN THIS");

        Boolean ex = rentDao.checkExists(rent.getProperty_id());
        System.out.println("ex is " + ex.toString());
        return null;
    }
}
