package rentdeck.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Entity
public class Property {

    public enum Visibility {
        VISIBLE, HIDDEN, WAIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_seq")
    @SequenceGenerator(name = "p_seq", sequenceName = "property_sequence", allocationSize = 1)
    private Long property_id;

    @NotBlank
    @Size(min = 4, max = 25)
    String title;
    String description;
    String address;
    String county;
    String municipality;
    String settlement;
    String street;
    @NotBlank
    String pic_url;

    @NotNull
    @Range(min = 1, max = 999)
    Short room_count;

    @NotNull
    @Range(min = 1, max = 999)
    Short bed_count;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    Users users;

    @NotNull
    @Range(min = 5, max = 99999999)
    Long price;

//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    Visibility visibility = Visibility.VISIBLE;

}
