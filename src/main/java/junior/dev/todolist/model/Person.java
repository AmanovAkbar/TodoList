package junior.dev.todolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="person",uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="username")
    private String username;

    @NotBlank
    @Size(max=120)
    @Column(name="password")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="person_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set <Role> roles=new HashSet<>();

    public Person(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Person() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString(){
        return username;
    }

}
