package hello;

import hello.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyDao extends CrudRepository<Property, Long> {}
