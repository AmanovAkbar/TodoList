package junior.dev.todolist.repository;

import junior.dev.todolist.model.ERole;
import junior.dev.todolist.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole role);
}
