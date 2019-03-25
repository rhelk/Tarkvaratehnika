package rentdeck.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import rentdeck.model.Users;

public interface AuthorityDao extends JpaRepository<Users, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO authorities VALUES (?1, 'ROLE_USER'))", nativeQuery = true)
    void addUserToUserRole(String username);
}