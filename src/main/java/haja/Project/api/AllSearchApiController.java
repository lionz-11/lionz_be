package haja.Project.api;

import haja.Project.domain.*;
import haja.Project.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AllSearchApiController {

    private final TasknoticeService tasknoticeService;
    private final TaskService taskService;
    private final Tasknotice_TagService tasknotice_tagService;


    @io.swagger.v3.oas.annotations.tags.Tag(name = "통합검색")
    @Operation(summary = "검색모달에서 키워드 검색 부분", description = "Tag, ")
    @GetMapping("all")
    public Result AllSearch(@RequestParam String word){
        List<Tasknotice> tasknotices = tasknoticeService.findByWord(word);
        List<TasknoticeDto> collect = tasknotices.stream()
                .map(t -> new TasknoticeDto(t))
                .collect(Collectors.toList());

        List<Task> tasks = taskService.findByWord(word);
        List<TaskDTO> collect2 = tasks.stream()
                .map(t -> new TaskDTO(t))
                .collect(Collectors.toList());


        List<Tasknotice_Tag> tnts  = tasknotice_tagService.findByTagName(word);
        List<Tasknotice_TagDto> collect3 = tnts.stream()
                .map(t -> new Tasknotice_TagDto(t))
                .collect(Collectors.toList());

        return new Result(collect,collect2,collect3);
    }




    @Data
    @AllArgsConstructor
    static class TasknoticeDto {
        private Long id;
        private Member member;
        private LocalDateTime date;
        private LocalDateTime deadline;
        //private LocalDateTime updatetime;
        private Part target;
        private File image;
        private String title;
        private String explanation;
        public TasknoticeDto(Tasknotice tasknotice) {
            id = tasknotice.getId();
            member = tasknotice.getMember();
            date = tasknotice.getDate();
            deadline = tasknotice.getDeadline();
            //updatetime = tasknotice.getUpdateTime();
            target = tasknotice.getTarget();
            title = tasknotice.getTitle();
            explanation = tasknotice.getExplanation();
        }
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



    @Data
    @AllArgsConstructor
    static class Tasknotice_TagDto{
        private Long id;
        private Tasknotice tasknotice;
        private Tag tag;

        Tasknotice_TagDto(Tasknotice_Tag tasknotice_tag){
            id = tasknotice_tag.getId();
            tasknotice = tasknotice_tag.getTasknotice();
            tag = tasknotice_tag.getTag();
        }
    }
    @Data
    static class AllSearchRequest{
        private String word;
    }

    @Data
    @AllArgsConstructor
    static class Result<TN,T,TNT,TKT>{
        private TN tasknotice;
        private T task;
        private TNT tasknotice_tag;
    }



}
