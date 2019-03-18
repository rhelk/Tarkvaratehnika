package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

@CrossOrigin
@RestController
public class PropertyController {

    @Autowired
    private PropertyDao propertyDao;
    @Autowired
    private UserDao userDao;

    @PostMapping("api/property/add")
    public Property addProperty(@RequestBody Property property, Principal principal) {
        property.setUsers(userDao.findByUsername(principal.getName()));
        return propertyDao.save(property);
    }

    @GetMapping("api/property/get/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("api/properties")
    public List<Property> getAllProperty() {
        return propertyDao.findAll();
    }

    @GetMapping("api/properties/prices")
    public List<Property> searchByPrice(@RequestParam("start")long start, @RequestParam("end") long end) {
        return propertyDao.searchByPrice(start, end);
    }

    @GetMapping("api/properties/search")
    public List<Property> searchProperties( Property property) {
        return propertyDao.findAll(Example.of(property));
    }


}
