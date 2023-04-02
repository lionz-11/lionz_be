package haja.Project.api;

import haja.Project.domain.Member;
import haja.Project.domain.Task;
import haja.Project.domain.Tasknotice;
import haja.Project.repository.MemberRepository;
import haja.Project.service.TaskService;
import haja.Project.service.TasknoticeService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Task")
public class TaskApiController {

    private final TaskService taskService;
    private final TasknoticeService tasknoticeService;
    private final MemberRepository memberRepository;


    // Tasknotice에 들어가면 제출하기 버튼이 있을거고 그 제출하기 버튼 // Tasknotice의 id를 넘겨줘야함
    // 프론트가 해줄 수 있다고 가정하고 ㄱㄱ

    // 과제생성
    @Operation(summary = "과제 생성")
    @PostMapping("task")
    public CreateTaskResponse createTaskResponse(@RequestBody @Valid CreateTaskRequest request) {
        Task task = new Task();
        task.setLink(request.getLink());
        task.setExplanation(request.getExplanation());
        task.setMember(memberRepository.findById(SecurityUtil.getCurrentMemberId()).get());
        task.setDate(LocalDateTime.now());
        task.setTasknotice(tasknoticeService.findOne(request.getTasknotice_id()));
        Long id = taskService.save(task);

        return new CreateTaskResponse(id);
    }

    @Data
    static class CreateTaskRequest {
        private String link;
        private String explanation;
        private Long tasknotice_id;
    }

    @Data
    static class CreateTaskResponse {
        private Long id;

        CreateTaskResponse(Long id) {
            this.id = id;
        }
    }

    //수정하기 버튼 -> 이전내용 그대로 가져오기위함
    @Operation(summary = "id로 과제 조회")
    @GetMapping("task/{id}")
    public Task task(@PathVariable("id") Long id) {
        return taskService.findOne(id);
    }

    @Operation(summary = "과제 공지사항별 과제 조회")
    @GetMapping("task/tasknotice/{tasknotice_id}")
    public Result taskByTasknotice(@PathVariable("tasknotice_id") Long tasknotice_id) {
        List<Task> tasks = taskService.findByTasknotice(tasknotice_id);
        List<TaskDTO> collect = tasks.stream()
                .map(t -> new TaskDTO(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    //수정완료 버튼
    @Operation(summary = "과제 수정")
    @PutMapping("task/{id}")
    public UpdateResponse task(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateRequest request) {
        Task task;
        task = taskService.findOne(id);

        //updateTime 도 하나 만들어야할듯
        task.setLink(request.getLink());
        task.setExplanation(request.getExplanation());

        Long task_id = taskService.save(task);

    return new UpdateResponse(task_id);
    }

    @Data
    static class UpdateRequest{
        private String link;
        private String explanation;
        private List<String> tags;
    }

    @Data
    static class UpdateResponse{
        private Long id;
        UpdateResponse(Long id){
            this.id = id;
        }
    }



    //삭제
    @Operation(summary = "과제 삭제")
    @DeleteMapping("task/{id}")
    public void deleteTask(@PathVariable("id")Long id){
        taskService.delete(id);
    }

    @Operation(summary = "로그인한 유저가 제출한 과제 조회")
    @GetMapping("task/me")
    public Result readByMe() {
        List<Task> tasks = taskService.findByMember(SecurityUtil.getCurrentMemberId());
        List<TaskDTO> collect = tasks.stream()
                .map(t -> new TaskDTO(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Operation(summary = "모든 과제 조회")
    @GetMapping("task")
    public Result ReadTask(){
        List<Task> tasks = taskService.findAll();
        List<TaskDTO> collect = tasks.stream()
                .map(t -> new TaskDTO(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class TaskDTO{
        private Long id;
        private Member member;
        private String link;
        private String explanation;
        private Tasknotice tasknotice;
        TaskDTO(Task task){
            id = task.getId();
            member = task.getMember();
            link = task.getLink();
            explanation = task.getExplanation();
            tasknotice = task.getTasknotice();
        }
    }
}