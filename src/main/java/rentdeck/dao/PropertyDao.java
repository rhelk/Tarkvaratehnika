package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rentdeck.model.Property;

import java.util.List;
//import org.springframework.data.repository.CrudRepository;

public interface PropertyDao extends JpaRepository<Property, Long> {

    @Query("SELECT u FROM Property u WHERE u.price BETWEEN ?1 AND ?2 ORDER BY u.price")
    List<Property> searchByPrice( long start, long end);
}
