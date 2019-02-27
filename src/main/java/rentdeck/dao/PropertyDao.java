package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rentdeck.model.Property;
//import org.springframework.data.repository.CrudRepository;

public interface PropertyDao extends JpaRepository<Property, Long> {}
