package haja.Project.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import haja.Project.domain.*;
import haja.Project.repository.MemberRepository;
import haja.Project.repository.TaskRepository;
import haja.Project.repository.Task_TagRepository;
import haja.Project.repository.TasknoticeRepository;
import haja.Project.service.TagService;
import haja.Project.service.TaskService;
import haja.Project.service.Task_TagService;
import haja.Project.service.TasknoticeService;
import haja.Project.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class TaskApiController {

    private final TaskService taskService;
    private final TasknoticeService tasknoticeService;
    private final MemberRepository memberRepository;
    private final TagService tagService;
    private final Task_TagService task_tagService;


    // Tasknotice에 들어가면 제출하기 버튼이 있을거고 그 제출하기 버튼 // Tasknotice의 id를 넘겨줘야함
    // 프론트가 해줄 수 있다고 가정하고 ㄱㄱ

    // 과제생성
    @PostMapping("task")
    public CreateTaskResponse createTaskResponse(@RequestBody @Valid CreateTaskRequest request) {
        Task task = new Task();
        task.setLink(request.getLink());
        task.setExplaination(request.getExplanation());
        task.setMember(memberRepository.findById(SecurityUtil.getCurrentMemberId()).get());
        task.setDate(LocalDateTime.now());
        task.setTasknotice(tasknoticeService.findOne(request.getTasknotice_id()));
        Long id = taskService.save(task);

        List<String> tags = request.tags;
        //태크넣기
        if (tags != null) {
            for (String tag_name : tags) {
                if (tagService.findByName(tag_name) == null) { //태크 없는거면 새로생성
                    Tag tag = new Tag();
                    tag.setName(tag_name);
                    tagService.save(tag);

                    Task_Tag task_tag = new Task_Tag();
                    task_tag.setTask(taskService.findOne(id));
                    task_tag.setTag(tag);
                    task_tagService.save(task_tag);
                } else { //태그 있는거면
                    Task_Tag task_tag = new Task_Tag();
                    task_tag.setTask(taskService.findOne(id));
                    task_tag.setTag(tagService.findByName(tag_name));
                    task_tagService.save(task_tag);
                }
            }
        }

        return new CreateTaskResponse(id);
    }

    @Data
    static class CreateTaskRequest {
        private String link;
        private String explanation;
        @CreatedDate
        private LocalDateTime date;
        private Long tasknotice_id;
        private List<String> tags;

    }

    @Data
    static class CreateTaskResponse {
        private Long id;

        CreateTaskResponse(Long id) {
            this.id = id;
        }
    }

    //수정하기 버튼 -> 이전내용 그대로 가져오기위함
    @PostMapping("button/task/{id}")
    public Task task(@PathVariable("id") Long id) {
        return taskService.findOne(id);
    }


    //수정완료 버튼
    @PutMapping("task/{id}")
    public UpdateResponse task(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateRequest request) {
        Task task;
        task = taskService.findOne(id);

        //updateTime 도 하나 만들어야할듯
        task.setLink(request.getLink());
        task.setExplaination(request.getExplanation());

        Long task_id = taskService.save(task);

        List<Task_Tag> tts = task_tagService.findByTaskId(id);
        List<ttDTO> result = tts.stream()
                .map(t -> new ttDTO(t))
                .collect(Collectors.toList());

        for(int i=0; i<result.size();i++){
            Task_Tag tt = task_tagService.findOne(result.get(i).id);
            task_tagService.delete(tt);
        }

        List<String> tags = request.tags;
        //태크 수정
        if (tags != null) {
            for (String tag_name : tags) {
                if (tagService.findByName(tag_name) == null) { //태크 없는거면 새로생성
                    Tag tag = new Tag();
                    tag.setName(tag_name);
                    tagService.save(tag);

                    Task_Tag task_tag = new Task_Tag();
                    task_tag.setTask(taskService.findOne(id));
                    task_tag.setTag(tag);
                    task_tagService.save(task_tag);
                } else { //태그 있는거면
                    Task_Tag task_tag = new Task_Tag();
                    task_tag.setTask(taskService.findOne(id));
                    task_tag.setTag(tagService.findByName(tag_name));
                    task_tagService.save(task_tag);
                }
            }
        }

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
    @Data
    static class ttDTO{
        private Long id;
        public ttDTO(Task_Tag task_tag){
            this.id = task_tag.getId();
        }
    }


    //삭제
    @DeleteMapping("task")
    public void deleteTask(@PathVariable("id")Long id){
        task_tagService.deleteByTaskId(id);
        taskService.delete(id);
    }


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
            explanation = task.getExplaination();
            tasknotice = task.getTasknotice();
        }
    }
}