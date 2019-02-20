package hello;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_seq")
    @SequenceGenerator(name = "p_seq", sequenceName = "property_sequence", allocationSize = 1)
    private long property_id;

    String description;
    String address;
    String pic_url;

    int room_count;
    int bed_count;

    Long owner_id;
    // Price is in cents
    Long price;

}
