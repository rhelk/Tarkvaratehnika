package rentdeck.model;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "authorities")
public class Authority {

    private String authority = new String("ROLE_USER");

    public Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    @Override
    public String toString() {
        return "Authoritie{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
