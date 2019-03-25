package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rentdeck.model.Users;

public interface AuthorityDao extends JpaRepository<Users, Long> {
}