package junior.dev.todolist.repository;

import junior.dev.todolist.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
Optional<Person> findPersonByUsernameAndPassword(String username, String password);
Optional<Person> findPersonByUsername(String username);
boolean existsByUsername(String username);

}