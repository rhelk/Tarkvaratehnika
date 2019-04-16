package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rentdeck.model.Users;

public interface UserDao extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

}
