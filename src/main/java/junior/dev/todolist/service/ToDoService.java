package junior.dev.todolist.service;

import junior.dev.todolist.exception.ResourceNotFoundException;
import junior.dev.todolist.model.Person;
import junior.dev.todolist.model.ToDo;
import junior.dev.todolist.repository.FileRepository;
import junior.dev.todolist.repository.ToDoRepository;
import junior.dev.todolist.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    ToDoRepository toDoRepository;

    @Autowired
    PersonRepository personRepository;

    public ToDo createToDo(String description, long userId){
        Person person = personRepository.getReferenceById(userId);
        ToDo toDo = new ToDo(description, person);
        return toDoRepository.save(toDo);
    }
    public ToDo setDescription(long toDoId, String description){
        ToDo todo = toDoRepository.findById(toDoId).orElseThrow( ()->   new ResourceNotFoundException("Not found toDo with id = " + toDoId)   );
        todo.setDescription(description);
        return toDoRepository.save(todo);
    }
    public ToDo setDone(long toDoId, boolean isDone){
        ToDo todo = toDoRepository.findById(toDoId).orElseThrow( ()->   new ResourceNotFoundException("Not found toDo with id = " + toDoId)   );
        todo.setDone(isDone);
        return toDoRepository.save(todo);
    }

    public ToDo setRemoved(long toDoId, boolean isRemoved){
        ToDo todo = toDoRepository.findById(toDoId).orElseThrow( ()->   new ResourceNotFoundException("Not found toDo with id = " + toDoId)   );
        todo.setRemoved(isRemoved);
        return toDoRepository.save(todo);
    }

    public void deleteToDo(long id){
        toDoRepository.deleteById(id);
    }

    public List<ToDo> getDoneUserToDo(long userId){
        List<ToDo> list = toDoRepository.findByPersonIdAndIsDoneTrueAndIsRemovedFalse(userId);
        return list;
    }
    public List<ToDo> getUserToDo(long userId){
        List<ToDo> list = toDoRepository.findByPersonIdAndIsRemovedFalse(userId);
        return list;
    }
    public List<ToDo> getNotDoneUserToDo(long userId){
        List<ToDo> list = toDoRepository.findByPersonIdAndIsDoneFalseAndIsRemovedFalse(userId);
        return list;
    }
    public List<ToDo> getRemovedUserToDo(long userId){
        List<ToDo> list = toDoRepository.findByPersonIdAndIsRemovedTrue(userId);
        return list;
    }
    public ToDo getToDoById(long id){
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if(toDo.isPresent()){
            ToDo td = toDo.get();
            return td;
        }else{
            throw new ResourceNotFoundException("Todo not found!");
        }
    }
}
