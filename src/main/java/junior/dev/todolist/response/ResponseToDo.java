package junior.dev.todolist.response;

public class ResponseToDo {

    private String description;

    private String status;
    private String uri;

    public ResponseToDo(String description,  String status, String uri) {
        this.description = description;

        this.status = status;
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
