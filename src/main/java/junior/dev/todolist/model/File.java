package junior.dev.todolist.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="File")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "todoId")
    private ToDo todo;

    @Column(name="fileName")
    private String fileName;

    @Column(name="type")
    private String type;

    @Column(name="data")
    @Lob
    private byte[] data;

    public File(String fileName, String type, byte[] data, ToDo toDo){
        this.fileName = fileName;
        this.type = type;
        this.data = data;
        this.todo = toDo;
    }

    public File() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ToDo getTodo() {
        return todo;
    }

    public void setTodo(ToDo todo) {
        this.todo = todo;
    }

}
