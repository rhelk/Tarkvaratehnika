package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rentdeck.dao.UserDao;
import rentdeck.model.Property;
import rentdeck.dao.PropertyDao;
import rentdeck.model.Users;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@CrossOrigin
@RestController
public class PropertyController {

    @Autowired
    private PropertyDao propertyDao;

    @Autowired
    private UserDao userDao;

    @PostMapping("api/secure/property/add")
    public Property addProperty(@Valid @RequestBody Property property, Principal principal) {
        if (principal != null){
            property.setUsers(userDao.findByUsername(principal.getName()));
            return propertyDao.save(property);
        }
        return null;
    }

    @GetMapping("api/property/get/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("api/property/all")
    public List<Property> getAllVisibleProperty() {
        System.out.println("Got it");
        return propertyDao.findByVisibility(Property.Visibility.VISIBLE);
    }

    @GetMapping("api/property/prices")
    public List<Property> searchByPrice(@RequestParam("start")long start, @RequestParam("end") long end) {
        return propertyDao.searchByPrice(start, end);
    }

    @GetMapping("api/property/search")
    public List<Property> searchProperties( Property property, Principal principal) {
        if (property.getUsers() != null) throw new ResponseStatusException((HttpStatus.FORBIDDEN));
        return propertyDao.findAll(Example.of(property, ExampleMatcher.matchingAll().withIgnoreCase()));
    }

     @GetMapping("api/property/mine")
     public List<Property> myProperties(Principal principal) {

        System.out.println("In here");

        if (principal == null) {throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}

        System.out.println(principal);

        Property property = new Property();
        property.setUsers(userDao.findByUsername(principal.getName()));

        return propertyDao.findAll(Example.of(property, ExampleMatcher.matchingAll().withIgnoreCase()));

     }

    @PostMapping("api/property/hidden/{property_id}")
    public Boolean hideProperty(@PathVariable Long property_id, Principal principal) {

        Property property = propertyDao.findById(property_id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!property.getUsers().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return propertyDao.setHidden(property_id) == 1;
    }

    @PostMapping("api/property/visible/{property_id}")
    public Boolean showProperty(@PathVariable Long property_id, Principal principal) {

        Property property = propertyDao.findById(property_id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!property.getUsers().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return propertyDao.setVisible(property_id) == 1;
    }

}
