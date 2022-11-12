package junior.dev.todolist.controller;

import junior.dev.todolist.exception.ResourceNotFoundException;
import junior.dev.todolist.model.File;
import junior.dev.todolist.model.ToDo;
import junior.dev.todolist.response.ResponseFile;
import junior.dev.todolist.response.ResponseMessage;
import junior.dev.todolist.response.ResponseToDo;
import junior.dev.todolist.security.service.UserDetailsImpl;
import junior.dev.todolist.service.FileService;
import junior.dev.todolist.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ToDoController {
    @Autowired
    private FileService fileService;
    @Autowired
    private ToDoService toDoService;

    @GetMapping("/")
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
        attributes.addAttribute("attribute", "redirectWithRedirectView");
        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            return new RedirectView("index.html");
        }else{
            return new RedirectView("login_register.html");
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}/todo")
    public ResponseEntity<List<ResponseToDo>> getToDoList(@PathVariable long userid){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getId()!=userid){
            throw new IllegalArgumentException("You are tryting to access another user's data!");
        }
        List<ResponseToDo> todos = toDoService.getUserToDo(userid).stream().map(ToDo->{
            String todoApiUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/todo/").path(String.valueOf(ToDo.getId())).toUriString();
            String status = "";
            if(ToDo.isDone()){
                status="1";
            }else{
                status="0";
            }

            return new ResponseToDo(ToDo.getDescription(), status, todoApiUri);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}/donetodo")
    public ResponseEntity<List<ResponseToDo>> getDoneToDoList(@PathVariable long userid){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getId()!=userid){
            throw new IllegalArgumentException("You are tryting to access another user's data!");
        }
        List<ResponseToDo> todos = toDoService.getDoneUserToDo(userid).stream().map(ToDo-> {
            String todoApiUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/todo/").path(String.valueOf(ToDo.getId())).toUriString();
            return new ResponseToDo(ToDo.getDescription(), "1", todoApiUri);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}/notdonetodo")
    public ResponseEntity<List<ResponseToDo>> getNotDoneToDoList(@PathVariable long userid){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getId()!=userid){
            throw new IllegalArgumentException("You are tryting to access another user's data!");
        }
        List<ResponseToDo> todos = toDoService.getNotDoneUserToDo(userid).stream().map(ToDo-> {
            String todoApiUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/todo/").path(String.valueOf(ToDo.getId())).toUriString();
            return new ResponseToDo(ToDo.getDescription(), "0", todoApiUri);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{userid}/trashbin")
    public ResponseEntity<List<ResponseToDo>> getRemovedToDoList(@PathVariable long userid){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getId()!=userid){
            throw new IllegalArgumentException("You are tryting to access another user's data!");
        }
        List<ResponseToDo> todos = toDoService.getRemovedUserToDo(userid).stream().map(ToDo->{
            String todoApiUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/todo/").path(String.valueOf(ToDo.getId())).toUriString();
            String status = "";
            if(ToDo.isDone()){
                status="1";
            }else{
                status="0";
            }
            return new ResponseToDo(ToDo.getDescription(), status, todoApiUri);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/todo/{todoid}/file")
    public ResponseEntity<ResponseFile> getFileInfo(@PathVariable long todoid){
        File file = fileService.getFileByToDoId(todoid);
        if (file == null){
            System.out.println("file is null");
            throw new ResourceNotFoundException("No such file!");
        }
        String fileApiUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/").path(String.valueOf(file.getId())).toUriString();
        ResponseFile responseFile = new ResponseFile(file.getFileName(), fileApiUri, file.getType());
        System.out.println(file.getFileName() + " " + fileApiUri + " " + file.getType());
        return ResponseEntity.status(HttpStatus.OK).body(responseFile);
    }
    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/file/{fileid}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable long fileid){

        File file = fileService.getFile(fileid);
        String filename = file.getFileName();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(file.getData());
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{userid}/todo")
    public ResponseEntity<ResponseMessage> createToDo(@RequestParam String description, @PathVariable long userid){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getId()!=userid){
            throw new IllegalArgumentException("You are tryting to access another user's data!");
        }
        String message = "";
        try{
            ToDo toDo = toDoService.createToDo(description, userid);
            message = "ToDo successfully created!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "an error occured while creating a todo!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/todo/{todoid}/file")
    public ResponseEntity<ResponseMessage> uploadFile(@PathVariable long todoid, @RequestParam("file")MultipartFile multipartFile){

        String message = "";
        try{
            File file = fileService.saveFile(multipartFile, todoid);
            message = "Uploaded successfully!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + multipartFile.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/todo/{todoid}/edit")
    public ResponseEntity<ResponseMessage> edit(@RequestBody String description, @PathVariable long todoid){
        String message="";
        try{
            ToDo todo = toDoService.setDescription(todoid, description);
            message="Changes were saved";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during changing description!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/todo/{todoid}/done")
    public ResponseEntity<ResponseMessage> makeDone(@PathVariable long todoid){
        String message="";
        try{
            ToDo todo = toDoService.setDone(todoid, true);
            message="Status changed to done!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during changing status!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/todo/{todoid}/notdone")
    public ResponseEntity<ResponseMessage> makeNotDone(@PathVariable long todoid){
        String message="";
        try{
            ToDo todo = toDoService.setDone(todoid, false);
            message="Status changed to not done!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during changing status!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/todo/{todoid}/remove")
    public ResponseEntity<ResponseMessage> removeToDo(@PathVariable long todoid){
        String message="";
        try{
            ToDo todo = toDoService.setRemoved(todoid, true);
            message="Removed Todo!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during changing status!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/todo/{todoid}/recover")
    public ResponseEntity<ResponseMessage> recoverToDo(@PathVariable long todoid){
        String message="";
        try{
            ToDo todo = toDoService.setRemoved(todoid, false);
            message="Todo recovered!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during changing status!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("todo/{todoid}")
    public ResponseEntity<ResponseMessage>deleteToDo(@PathVariable long todoid){
        String message="";
        try{
            toDoService.deleteToDo(todoid);
            message="Todo deleted successfully";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }catch (Exception ex){
            message = "An error occured during deleting ToDo!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/file/{fileid}")
    public ResponseEntity<ResponseMessage>deleteFile(@PathVariable long fileid){

        fileService.deleteFile(fileid);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("file deleted successfully!"));
    }

}
