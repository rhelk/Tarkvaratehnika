package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import rentdeck.model.Property;

import java.util.List;

public interface PropertyDao extends JpaRepository<Property, Long> {

    @Query("SELECT u FROM Property u WHERE u.price BETWEEN ?1 AND ?2 ORDER BY u.price")
    List<Property> searchByPrice( long start, long end);

    List<Property>findByVisibility(Property.Visibility visibility);

    @Transactional
    @Modifying
    @Query("UPDATE Property SET Visibility = 'HIDDEN' WHERE property_id = ?1")
    Integer setHidden(long id);

    @Transactional
    @Modifying
    @Query("UPDATE Property SET Visibility = 'VISIBLE' WHERE property_id = ?1")
    Integer setVisible(Long property_id);
}
