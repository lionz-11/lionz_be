package haja.Project.api;

import haja.Project.domain.Part;
import haja.Project.domain.Tasknotice;
import haja.Project.domain.User;
import haja.Project.repository.TasknoticeRepository;
import haja.Project.service.TasknoticeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TasknoticeApiController {

    private final TasknoticeService tasknoticeService;
    private final TasknoticeRepository tasknoticeRepository;

    //create
    @PostMapping("/tasknotice")
    public CreateTasknoticeResponse createTasknotice(@RequestBody @Valid CreateTasknoticeRequest request) {
        Tasknotice tasknotice = new Tasknotice();
        tasknotice.setDeadline(request.deadline);
        tasknotice.setTarget(request.target);
        tasknotice.setImage(request.image);
        tasknotice.setTitle(request.title);
        tasknotice.setExplanation(request.explanation);

        Long id = tasknoticeService.join(tasknotice);
        return new CreateTasknoticeResponse(id);
    }

    //read
    @GetMapping("/tasknotice")
    public Result ReadTasknotice() {
        List<Tasknotice> tasknotices = tasknoticeService.findTasknotice();
        List<TasknoticeDto> collect = tasknotices.stream()
                .map(t -> new TasknoticeDto(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    //update
    @PutMapping("/tasknotice/{id}")
    public TasknoticeDto updateTasknotice(@PathVariable("id") Long id, @RequestBody @Valid TasknoticeRequest request) {
        tasknoticeService.update(id, request.getTarget(), request.getImage(), request.getTitle(), request.getExplanation());
        Tasknotice tasknotice = tasknoticeService.findOne(id);
        return new TasknoticeDto(tasknotice);
    }

    //delete
    @DeleteMapping("/tasknotice/{id}")
    public void deleteTasknotice(@PathVariable("id") Long id) {
        tasknoticeService.delete(id);
    }

    @Data
    static class CreateTasknoticeRequest {
        private LocalDateTime deadline;
        private Part target;
        private File image;
        private String title;
        private String explanation;
    }

    @Data
    static class CreateTasknoticeResponse {
        private Long id;

        public CreateTasknoticeResponse(Long id) { this.id = id; }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class TasknoticeDto {
        private Long id;
        private User user;
        private LocalDateTime date;
        private LocalDateTime deadline;
        private LocalDateTime updatetime;
        private Part target;
        private File image;
        private String title;
        private String explanation;
        private Long like;

        public TasknoticeDto(Tasknotice tasknotice) {
            id = tasknotice.getId();
            user = tasknotice.getUser();
            date = tasknotice.getDate();
            deadline = tasknotice.getDeadline();
            updatetime = tasknotice.getUpdateTime();
            target = tasknotice.getTarget();
            image = tasknotice.getImage();
            title = tasknotice.getTitle();
            explanation = tasknotice.getExplanation();
            like = tasknotice.getLike();
        }
    }

    @Data
    static class TasknoticeRequest {
        private Long id;
        private String name;
        private LocalDateTime date;
        private LocalDateTime deadline;
        private LocalDateTime updatetime;
        private Part target;
        private File image;
        private String title;
        private String explanation;
        private Long like;
    }

}
