package junior.dev.todolist.repository;

import junior.dev.todolist.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface FileRepository extends JpaRepository<File, Long> {
@Transactional
File findFileByTodoId(long todoId);


}