package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rentdeck.model.DateRanges;
import rentdeck.model.Property;
import rentdeck.model.Rent;

import java.sql.Date;
import java.util.List;

public interface RentDao extends JpaRepository<Rent, Long> {

    @Query("SELECT count(r)>0 FROM Rent r WHERE r.property.property_id = ?1 AND r.state <> 'DONE'")
    Boolean checkExists( Property property);

    @Query("SELECT r FROM Rent r WHERE (r.owner_id = ?1 AND r.state IN ('TO_RENT', 'INFORM_RENT')) " +
            "OR (r.renter_username = ?2 AND r.state IN ('DENY_RENT', 'CONFIRM_RENT'))")
    List<Rent> findAllByStateNotDone(long user_id, String renter_username);

    @Query("SELECT r FROM Rent r WHERE r.renter_username = ?2 OR r.owner_id = ?1 ORDER BY r.rent_id DESC ")
    List<Rent> findAllRent(long user_id, String renter_username);

    @Query("SELECT r FROM Rent r WHERE r.property.property_id = ?1")
    List<Rent> findByProperty_id(Long property_id);

    @Query("SELECT r FROM Rent r WHERE (?1 >= r.start AND ?1 <= r.end) " +
            "OR (?2 >= r.start AND ?2 <= r.end) OR (?1 < r.start AND ?2 > r.end)")
    List<Rent> findDateConflicts(Date start, Date end);

    @Query("SELECT r FROM Rent r WHERE r.end > CURRENT_DATE AND r.property.property_id = ?1 " +
            "AND r.state = 'CONFIRM_RENT'")
    List<Rent> findAllLaterThanToday(Long property_id);

}
