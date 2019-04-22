package rentdeck.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.sql.Date;

@Data
@Entity
public class Rent {

    public enum State {
        TO_RENT, DENY, CONFIRM, DONE,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "r_seq")
    @SequenceGenerator(name = "r_seq", sequenceName = "rent_sequence", allocationSize = 1)
    private Long rent_id;

    public Long renter;
    public Long property_id;
    public Long owner_id;

    @Enumerated(EnumType.STRING)
    public State state;

    //SQL Date so accuracy is day

    public Date start;
    public Date end;

}
