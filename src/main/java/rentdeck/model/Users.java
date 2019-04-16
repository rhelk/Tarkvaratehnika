package rentdeck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Users implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "users_sequence", allocationSize = 1)
    private Long user_id;

    String first_name;
    String last_name;
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "username",
                    referencedColumnName = "username")
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Authority> authorityList;


}
