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
import rentdeck.dao.PropertyDao;
import rentdeck.dao.RentDao;
import rentdeck.dao.UserDao;
import rentdeck.model.Rent;
import rentdeck.model.Users;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class RentController {

    @Autowired
    RentDao rentDao;

    @Autowired
    PropertyDao propertyDao;

    @Autowired
    UserDao userDao;

    @GetMapping("api/rent")
    public List<Rent> getAllRentRequests(Principal principal) {

        Users user = userDao.findByUsername(principal.getName());

        List<Rent> rents = rentDao.findAllByUserId(user.getUser_id());
        List<Rent> result = new ArrayList<>();

        // INFORM_RENT is state after Renter has acknowledges confirmal of rent,
        // but before owner knows that.
        rents.forEach(rent -> {

            result.add(rent.makeClone());

            if (rent.getState() == Rent.State.INFORM_RENT
                    && rent.getOwner_id() == user.getUser_id()) {
                rent.setState(Rent.State.DONE);
                rentDao.save(rent);
            }

        });

        return result;
    }

    @GetMapping("api/rent/dates/{id]")
    public List<Rent> datesLocked(@PathVariable Long id) {

        System.out.println("In this");

        return rentDao.findAllLaterThanToday(id);
    }

    @GetMapping("api/rent/confirm/{rent_id]")
    public void confirmRent(@PathVariable Long rent_id, Principal principal) {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Make sure requester is actually owner of the property in question
        if (userDao.findByUsername(principal.getName()).getUser_id() != rent.getOwner_id())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        List<Rent> sameProperty  = rentDao.findByProperty_id(rent.getProperty().getProperty_id());
        sameProperty.forEach(rent1 -> {
            if (rent.getState() == Rent.State.TO_RENT) rentDao.delete(rent);
        });

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.CONFIRM_RENT);
            rentDao.save(rent);

            List<Rent> Others = rentDao.findDateConflicts(rent.start, rent.end);
            Others.forEach(rent1 -> rentDao.delete(rent));



        }
    }

    @GetMapping("api/rent/deny/{rent_id]")
    public void denyRent(@PathVariable Long rent_id, Principal principal) {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Make sure requester is actually owner of the property in question
        if (userDao.findByUsername(principal.getName()).getUser_id() != rent.getOwner_id())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.DENY_RENT);
            rentDao.save(rent);
        }

    }

}
