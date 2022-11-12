package junior.dev.todolist.repository;

import junior.dev.todolist.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByIsDoneTrue();
    List<ToDo> findByIsDoneFalse();
    List<ToDo> findByIsRemovedTrue();
    List<ToDo> findByIsRemovedFalse();
    List<ToDo> findByPersonId(long id);
    List<ToDo> findByPersonIdAndIsDoneTrueAndIsRemovedFalse(long userId);
    List<ToDo> findByPersonIdAndIsDoneFalseAndIsRemovedFalse(long userId);
    List<ToDo> findByPersonIdAndIsRemovedTrue(long userId);
    List<ToDo> findByPersonIdAndIsRemovedFalse(long userId);

}