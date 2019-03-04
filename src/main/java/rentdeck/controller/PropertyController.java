package rentdeck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import rentdeck.model.Property;
import rentdeck.dao.PropertyDao;

import java.util.List;

@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class PropertyController {

    @Autowired
    private PropertyDao propertyDao;

    @PostMapping("api/property/add")
    public Property addProperty(@RequestBody Property property) {
        return propertyDao.save(property);
    }

    @GetMapping("api/property/get/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        Property property = propertyDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        property.getUser().setPassword(null);
        return property;
    }

    @GetMapping("api/properties")
    public List<Property> getAllProperty() {
        List<Property> properties = propertyDao.findAll();
        properties.forEach(property -> property.getUser().setPassword(null));
        return properties;
    }

    @GetMapping("api/properties/prices")
    public List<Property> searchByPrice(@RequestParam("start")int start, @RequestParam("end") int end) {
        return propertyDao.searchByPrice(start, end);
    }
}
