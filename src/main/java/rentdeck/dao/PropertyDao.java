package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rentdeck.model.Property;

import java.util.List;
//import org.springframework.data.repository.CrudRepository;

public interface PropertyDao extends JpaRepository<Property, Long> {

    @Query("SELECT u FROM User u WHERE u.price BETWEEN :start AND :end")
    List<Property> searchByPrice(int start, int end);
}
