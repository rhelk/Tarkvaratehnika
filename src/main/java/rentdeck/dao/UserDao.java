package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rentdeck.model.User;

public interface UserDao extends JpaRepository<User, Long> {}
