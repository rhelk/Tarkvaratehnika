package rentdeck.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Users implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    @SequenceGenerator(name = "my_seq", sequenceName = "users_sequence", allocationSize = 1)
    private Long user_id;

    @NotBlank
    @Size(min = 2)
    String first_name;

    @NotBlank
    @Size(min = 2)
    String last_name;

    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
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
