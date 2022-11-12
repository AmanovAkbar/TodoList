package junior.dev.todolist.service;

import junior.dev.todolist.exception.ResourceNotFoundException;
import junior.dev.todolist.model.File;
import junior.dev.todolist.model.ToDo;
import junior.dev.todolist.repository.FileRepository;
import junior.dev.todolist.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ToDoRepository toDoRepository;

    public File saveFile(MultipartFile multipartFile, long toDoId) throws IOException {
        ToDo toDo = toDoRepository.getReferenceById(toDoId);
        File file = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getBytes(), toDo);
        return fileRepository.save(file);
    }

    public File getFile(long id){
        Optional<File> file = fileRepository.findById(id);
        if(file.isPresent()){
            return fileRepository.findById(id).get();
        }else{
            throw new ResourceNotFoundException("File not found!");
        }


    }
    public void deleteFile(long id){
        fileRepository.deleteById(id);
    }

    public Stream<File> getAllFiles(){
        return fileRepository.findAll().stream();
    }

    public File getFileByToDoId(long id){ return fileRepository.findFileByTodoId(id);}


}
