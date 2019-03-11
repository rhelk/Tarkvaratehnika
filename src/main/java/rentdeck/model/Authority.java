package rentdeck.model;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "authorities")
public class Authority {
    private String authority;

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
