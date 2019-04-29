package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    public JavaMailSender emailSender;

    @GetMapping("api/rent")
    public List<Rent> getAllRentRequests(Principal principal) {
        Users user = userDao.findByUsername(principal.getName());
        return rentDao.findAllRent(user.getUser_id(), user.getUsername());
    }

    @PostMapping("api/rent/to_rent/{property_id}")
    public Rent initiateRent(@RequestBody Rent rent, @PathVariable Long property_id, Principal principal) {

        rent.setProperty(propertyDao.findById(property_id)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND))));
        rent.setOwner_id(rent.getProperty().getUsers().getUser_id());
//        rent.setRenter_id(userDao.findByUsername(principal.getName()).getUser_id());
        rent.setRenter_username(principal.getName());
        rent.setState(Rent.State.TO_RENT);

//        sendEmail("rohelk@ttu.ee", "prooviks", "See siin");
//        return null;

        return rentDao.save(rent);
    }

    @GetMapping("api/rent/dates/{property_id}")
    public List<Rent> datesLocked(@PathVariable Long property_id) {

        System.out.println("In this");

        return rentDao.findAllLaterThanToday(property_id);
    }

    @PostMapping("api/rent/confirm/{rent_id]")
    public void confirmRent(@PathVariable Long rent_id, Principal principal) {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Make sure requester is actually owner of the property in question
        if (userDao.findByUsername(principal.getName()).getUser_id() != rent.getOwner_id())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

//        List<Rent> sameProperty  = rentDao.findByProperty_id(rent.getProperty().getProperty_id());
//        sameProperty.forEach(rent1 -> {
//            if (rent.getState() == Rent.State.TO_RENT) rentDao.delete(rent);
//        });

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.CONFIRM_RENT);
            rentDao.save(rent);

            List<Rent> Others = rentDao.findDateConflicts(rent.start, rent.end);
            Others.forEach(rent1 -> {
                if (rent1.getState() == Rent.State.TO_RENT)
                    rent1.setState(Rent.State.DENY_RENT);
            });
        }
    }

    @PostMapping("api/rent/deny/{rent_id]")
    public void denyRent(@PathVariable Long rent_id, Principal principal) {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Make sure requester is actually owner of the property in question
        if (userDao.findByUsername(principal.getName()).getUser_id() != rent.getOwner_id())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.DENY_RENT);
            rentDao.save(rent);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    }

    private void sendEmail(String destination, String subject, String contents) throws Exception{

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destination);
        message.setSubject(subject);
        message.setText(contents);
        emailSender.send(message);

    }

}
