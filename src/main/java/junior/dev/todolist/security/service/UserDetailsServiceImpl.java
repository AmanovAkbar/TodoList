package junior.dev.todolist.security.service;

import junior.dev.todolist.model.Person;
import junior.dev.todolist.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    PersonRepository personRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person user = personRepository.findPersonByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("Username not found"));
        return UserDetailsImpl.build(user);

    }
}
