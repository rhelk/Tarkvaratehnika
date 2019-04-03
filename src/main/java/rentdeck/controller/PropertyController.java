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
    public Property addProperty(@RequestBody Property property, Principal principal) {
        if (principal != null){
            property.setUsers(userDao.findByUsername(principal.getName()));
        } else {
//            System.out.println("Principal is " + principal);
        }
        return propertyDao.save(property);
    }

    @GetMapping("api/property/get/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("api/property/all")
    public List<Property> getAllVisibleProperty() {
        return propertyDao.findByVisibility(Property.Visibility.VISIBLE);
    }

    @GetMapping("api/property/prices")
    public List<Property> searchByPrice(@RequestParam("start")long start, @RequestParam("end") long end) {
        return propertyDao.searchByPrice(start, end);
    }

    @GetMapping("api/property/search")
    public List<Property> searchProperties( Property property, Principal principal) {
        System.out.println("Property is " + property);
        if (Stream.of(property.getPrice(), property.getAddress(), property.getBed_count(), property.getCounty(),
                  property.getDescription(), property.getMunicipality(), property.getPic_url(), property.getProperty_id(),
                  property.getRoom_count(), property.getSettlement(), property.getStreet(), property.getTitle())
//                .peek(System.out::println)
                .allMatch(Objects::isNull)) {
            if (property.getUsers().getUser_id() == null) {
                property.setUsers(userDao.findByUsername(principal.getName()));
//                System.out.println("NOW " + property.getUsers());
            }
//            System.out.println("All Null");
        }

        return propertyDao.findAll(Example.of(property, ExampleMatcher.matchingAll().withIgnoreCase()));
    }

    @PostMapping("api/property/rent")
    public Boolean rentProperty(@RequestBody String id) {
        id = id.replaceAll("[{}=\"]","");
        return propertyDao.setHidden(Long.valueOf(id)) == 1;
    }

}
