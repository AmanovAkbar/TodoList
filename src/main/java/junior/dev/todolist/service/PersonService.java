package junior.dev.todolist.service;

import junior.dev.todolist.exception.ResourceNotFoundException;
import junior.dev.todolist.model.Person;
import junior.dev.todolist.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person createPerson(String name, String password){
        if(!personRepository.existsByUsername(name)){
            return personRepository.save(new Person(name, password));
        }else{
            throw new ResourceNotFoundException("User already exists");
        }

    }
    public void deletePerson(long id){
        personRepository.deleteById(id);
    }

    public Person updatePassword(String password, long id){
        Person person = personRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User already exists"));
        person.setPassword(password);
        return personRepository.save(person);
    }
}
