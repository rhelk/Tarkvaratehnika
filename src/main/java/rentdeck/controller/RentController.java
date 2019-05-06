package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rentdeck.dao.PropertyDao;
import rentdeck.dao.RentDao;
import rentdeck.dao.UserDao;
import rentdeck.model.DateRanges;
import rentdeck.model.Rent;
import rentdeck.model.Users;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class RentController {

    private final String E_TO_RENT_SUBJ = "[RentDeck] You have received request to Rent one of your properties";
    private final String E_DENY_RENT_SUBJ = "[RentDeck] A request to rent has been denied.";
    private final String E_CONFIRM_RENT_SUBJ = "[RentDeck] A request to rent has been accepted.";

    private final String E_CANCEL_RENT_SUBJ_RENTER = "[RentDeck] You have cancelled a rent request";
    private final String E_CANCEL_RENT_SUBJ_OWNER = "[RentDeck] Rent request to your property has been withdrawn";

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
    public Rent initiateRent(@RequestBody Rent rent, @PathVariable Long property_id, Principal principal) throws Exception {

        if (rent.start == null || rent.end == null ||
                rent.start.toLocalDate().compareTo(rent.end.toLocalDate()) >= 0)
            throw new ResponseStatusException((HttpStatus.BAD_REQUEST));

        rent.setProperty(propertyDao.findById(property_id)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND))));

        rent.setOwner_id(rent.getProperty().getUsers().getUser_id());

        String ownerUsername = rent.getProperty().getUsers().getUsername();
        if (ownerUsername.equals(principal.getName()))
            throw new ResponseStatusException((HttpStatus.NOT_FOUND));

        rent.setRenter_username(principal.getName());
        rent.setState(Rent.State.TO_RENT);
        rentDao.save(rent);

        String emailContent = "Someone has made request to rent your property titled \"" +
                rent.getProperty().getTitle() + "\" between " + rent.getStart().toString() +
                " and " + rent.getEnd().toString();

        System.out.println("Email to: " + ownerUsername);
        System.out.println("Email subject: " + E_TO_RENT_SUBJ);
        System.out.println("Email content: " + emailContent);

        sendEmail(ownerUsername, E_TO_RENT_SUBJ, emailContent);

        return rent;
    }

    @GetMapping("api/rent/dates/{property_id}")
    public List<DateRanges> datesLocked(@PathVariable Long property_id) {

        List<Rent> rents = rentDao.findAllLaterThanToday(property_id);
        List<DateRanges> dateRanges = new ArrayList<>();

        rents.forEach(rent -> {
            DateRanges singleRange = new DateRanges();
            singleRange.setStart(rent.getStart());
            singleRange.setEnd(rent.getEnd());
            dateRanges.add(singleRange);
        });

        return dateRanges;

    }

    @PostMapping("api/rent/confirm/{rent_id}")
    public void confirmRent(@PathVariable Long rent_id, Principal principal) throws Exception {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Make sure requester is actually owner of the property in question

        String ownerUsername = rent.getProperty().getUsers().getUsername();
        if (!principal.getName().equals(ownerUsername))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.CONFIRM_RENT);
            rentDao.save(rent);

            List<Rent> Others = rentDao.findDateConflicts(rent.start, rent.end);
            Others.forEach(rent1 -> {
                if (rent1.getState() == Rent.State.TO_RENT) {
                    rent1.setState(Rent.State.DENY_RENT);
//                    rent1.renter_username

                    String emailContent = "Owner of property titled \"" +
                            rent.getProperty().getTitle() + "\" has rejected your rent request.";

                    System.out.println("Email to: " + rent.getRenter_username());
                    System.out.println("Email Subject: " + E_DENY_RENT_SUBJ);
                    System.out.println("Email content: " + emailContent);

                    try {
                        sendEmail(rent1.getRenter_username(), E_DENY_RENT_SUBJ, emailContent);
                    } catch (Exception e) {
                        System.out.println("Error sending email inside lambda: " + e.toString());
                    }
                }
            });

            String emailContent = "Someone has made request to rent your property titled \"" +
                    rent.getProperty().getTitle() + "\" between " + rent.getStart().toString() +
                    " and " + rent.getEnd().toString();

            System.out.println("Email to: " + rent.getRenter_username());
            System.out.println("Email Subject: " + E_CONFIRM_RENT_SUBJ);
            System.out.println("Email content: " + emailContent);

            sendEmail(rent.getRenter_username(), E_CONFIRM_RENT_SUBJ, emailContent);

        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("api/rent/deny/{rent_id}")
    public void denyRent(@PathVariable Long rent_id, Principal principal) throws Exception {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        String ownerUsername = rent.getProperty().getUsers().getUsername();
        // Make sure requester is actually owner of the property in question
        if (!ownerUsername.equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (rent.state == Rent.State.TO_RENT) {
            rent.setState(Rent.State.DENY_RENT);
            rentDao.save(rent);

            String emailContent = "Owner of property titled \"" +
                    rent.getProperty().getTitle() + "\" has rejected your rent request.";

//            System.out.println("Email to: " +rent.getRenter_username());
//            System.out.println("Email subject: " +E_DENY_RENT_SUBJ);
//            System.out.println("Email content: " +emailContent);

            sendEmail(rent.getRenter_username(), E_DENY_RENT_SUBJ, emailContent);

        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("api/rent/{rent_id}")
    public void cancelRentRequest(@PathVariable Long rent_id, Principal principal) throws Exception {

        Rent rent = rentDao.findById(rent_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!principal.getName().equals(rent.renter_username))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if (rent.getState() == Rent.State.TO_RENT || (rent.getState() == Rent.State.CONFIRM_RENT
                && rent.getStart().toLocalDate().compareTo(LocalDateTime.now().toLocalDate()) > 0)) {
            rentDao.delete(rent);

            String emailContent = "You have removed your request to rent property titled \"" +
                    rent.getProperty().getTitle() + "\"";

            System.out.println("Email to: " + rent.getRenter_username());
            System.out.println("Email subject: " + E_CANCEL_RENT_SUBJ_RENTER);
            System.out.println("Email content: " + emailContent);

            sendEmail(rent.getRenter_username(), E_CANCEL_RENT_SUBJ_RENTER, emailContent);

            emailContent = "rent request to your property titled \"" +
                    rent.getProperty().getTitle() + "\" has been cancelled.";

            System.out.println("Email to: " + rent.getProperty().getUsers().getUsername());
            System.out.println("Email subject: " + E_CANCEL_RENT_SUBJ_OWNER);
            System.out.println("Email content: " + emailContent);

            sendEmail(rent.getProperty().getUsers().getUsername(),
                    E_CANCEL_RENT_SUBJ_OWNER, emailContent);

        }

    }

    private void sendEmail(String destination, String subject, String contents) throws Exception{

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destination);
        message.setSubject(subject);
        message.setText(contents);
        emailSender.send(message);

    }

}
