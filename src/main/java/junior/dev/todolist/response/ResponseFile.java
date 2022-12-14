package junior.dev.todolist.response;

public class ResponseFile {

    private String filename;

    private String url;

    private String type;

    public ResponseFile(String filename, String url, String type){
        this.filename = filename;
        this.url = url;
        this.type = type;

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
