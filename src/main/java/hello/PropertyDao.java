package hello;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

public interface PropertyDao extends JpaRepository<Property, Long> {}
