package rentdeck;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "users_sequence", allocationSize = 1)
    private long user_id;

    String first_name;
    String last_name;
    String email;
    String password;

}
