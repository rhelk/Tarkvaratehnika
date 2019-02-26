package rentdeck.dao;

import org.springframework.data.repository.CrudRepository;
import rentdeck.model.User;

public interface UserDao extends CrudRepository<User, Long> {}
