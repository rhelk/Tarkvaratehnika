package rentdeck.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.CascadeType;
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


@Data
@Entity
public class Property {

    public enum Visibility {
        VISIBLE, HIDDEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_seq")
    @SequenceGenerator(name = "p_seq", sequenceName = "property_sequence", allocationSize = 1)
    private Long property_id;

    String title;
    String description;
    String address;
    String county;
    String municipality;
    String settlement;
    String street;
    String pic_url;

    Integer room_count;
    Integer bed_count;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    Users users;

    // Price is in cents
    Long price;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    Visibility visibility = Visibility.VISIBLE;

}
