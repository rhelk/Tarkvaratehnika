package hello;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
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
        return propertyDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("api/properties")
    public Iterable<Property> getAllProperty() {
        return propertyDao.findAll();
    }
}
