package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rentdeck.model.Rent;

public interface RentDao extends JpaRepository<Rent, Long> {

    @Query("SELECT count(r)>0 FROM Rent r WHERE r.property_id = ?1 AND r.state <> 'DONE'")
    Boolean checkExists( long property);
}
