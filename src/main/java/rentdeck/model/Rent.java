package rentdeck.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.sql.Date;

@Data
@Entity
public class Rent {

    public enum State {
        TO_RENT, DENY_RENT, CONFIRM_RENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "r_seq")
    @SequenceGenerator(name = "r_seq", sequenceName = "rent_sequence", allocationSize = 1)
    private Long rent_id;

    public String renter_username;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "property_id")
    Property property;

    public Long owner_id;

    @Enumerated(EnumType.STRING)
    public State state;

    @Column(name = "rent_start")
    public Date start;

    @Column(name = "rent_end")
    public Date end;

    public Rent makeClone() {
        Rent result = new Rent();

        result.setRent_id(this.rent_id);
        result.setRenter_username(this.renter_username);
        result.setOwner_id(this.owner_id);
        result.setState(this.state);
        result.setProperty(this.property);
        result.setStart(this.start);
        result.setEnd(this.end);

        return result;
    }
}
