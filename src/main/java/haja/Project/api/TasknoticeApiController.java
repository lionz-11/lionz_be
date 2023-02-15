package haja.Project.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import haja.Project.domain.*;
import haja.Project.repository.TasknoticeRepository;
import haja.Project.repository.Tasknotice_TagRepository;
import haja.Project.repository.UserRepository;
import haja.Project.service.*;
import haja.Project.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController // Rest API를 처리하는 Controller 임을 의미
@RequiredArgsConstructor
public class TasknoticeApiController {

    private final TasknoticeService tasknoticeService;
    //private final UserService userService;
    private final MemberService memberService;
    private final Tasknotice_TagService tasknotice_tagService;
    private final TagService tagService;

    //               <과제공지글 생성>
    //어차피 request.뭐시기 해서 일일히 다 넣어줘야해서 service패키지에 메서드 안만들었음
    @PostMapping("tasknotice")
    public CreateTasknoticeResponse createTasknotice(@RequestBody @Valid CreateTasknoticeRequest request){
        Tasknotice tasknotice = new Tasknotice();
        //tag_id만 리스트로 받고
        //태그버튼을 누르면 tasknotice_tag객체가 생성되게 하고
        tasknotice.setMember(memberService.findById(SecurityUtil.getCurrentMemberId()).get());
        //System.out.println(tasknotice.getMember().getEmail()); -> setMember 잘 됐는지 테스트용/ 잘 되는거 확인했습니다
        tasknotice.setDate(LocalDateTime.now()); //생성시점
        tasknotice.setDeadline(request.getDeadline()); // postman으로 날짜받는거
        tasknotice.setTarget(request.getTarget());
        tasknotice.setTitle(request.getTitle());
        tasknotice.setExplanation(request.getExplanation());

        Long tn_id = tasknoticeService.save(tasknotice);
        //이러면 이제 과제공지글을 생성이 된거고 이 때 request로 tag_id받고 아래에 tasknoticetag로직 추가
        //request로 tag_id를 어떻게받을까 List로 받아서 for문으로 돌리자

        for(Long e : request.tag_idList){
            Tasknotice_Tag tasknotice_tag = new Tasknotice_Tag();
            tasknotice_tag.setTasknotice(tasknoticeService.findOne(tn_id));
            tasknotice_tag.setTag(tagService.findOne(e));
            tasknotice_tagService.save(tasknotice_tag);
        }

        return new CreateTasknoticeResponse(tn_id);  // new 조심
    }
    @Data
    static class CreateTasknoticeRequest{
        //private User user; //원래 이렇게 객체로 받고싶었는데 postman으로 그게 안돼서
        //private Long user_id;  // -> request로 user 정보를 받는게 아니니까 빼도 될듯

        @CreatedDate
        private LocalDateTime date;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //데드라인을 어떤형식으로 받을지 명시
        private LocalDateTime deadline;
        private Part target;
        //private File image; // 이건 나중에
        private String title;
        private String explanation;

        private List<Long> tag_idList; //tag id 리스트로 받아서 위에서 스트림으로 돌려서 넣자
    }

    @Data
    static class CreateTasknoticeResponse{
        private Long id;
        public CreateTasknoticeResponse(Long id){
            this.id = id;
        }
    }

    @PostMapping("button/tasknotice/{id}") //수정하기 버튼
    public Tasknotice tasknotice(
            @PathVariable("id") Long id){

        return tasknoticeService.findOne(id); //이렇게 하면 Json이 그대로 오는것을 확인함
        //그니까 수정하기 버튼 누르면 이렇게 주고
        //수정완료 버튼 누르면 request날라온거 set으로 ㄱㄱ
    }

    @PutMapping("tasknotice/{id}")
    public UpdateResponse tasknotice(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateRequest request){
        Tasknotice tn;
        tn = tasknoticeService.findOne(id);

        //updateTime 도 하나 만들어야할듯
        tn.setDeadline(request.getDeadline()); // postman으로 날짜받는거
        tn.setTarget(request.getTarget());
        tn.setTitle(request.getTitle());
        tn.setExplanation(request.getExplanation());

        Long tn_id = tasknoticeService.save(tn);
        //이러면 이제 과제공지글을 생성이 된거고 이 때 request로 tag_id받고 아래에 tasknoticetag로직 추가
        //request로 tag_id를 어떻게받을까 List로 받아서 for문으로 돌리자

        List<Tasknotice_Tag> tts = tasknotice_tagService.findById(id); //여기부터 tasknotice_tag삭제 후 생성
        List<ttDTO> result = tts.stream()
                .map(t -> new ttDTO(t))
                .collect(Collectors.toList());

        for(int i=0; i<result.size();i++) {
            Tasknotice_Tag tt = tasknotice_tagService.findOne(result.get(i).id);
            tasknotice_tagService.delete(tt);
        }

        for(Long e : request.tag_idList){
            Tasknotice_Tag tasknotice_tag = new Tasknotice_Tag();
            tasknotice_tag.setTasknotice(tasknoticeService.findOne(tn_id));
            tasknotice_tag.setTag(tagService.findOne(e));
            tasknotice_tagService.save(tasknotice_tag);
        }
        return new UpdateResponse(tn_id);
    }

    @Data
    static class UpdateRequest{
        private Long id;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;
        private Part target;
        //private File image; // 이건 나중에
        private String title;
        private String explanation;

        private List<Long> tag_idList;
        private List<Long> data;
    }

    @Data
    static class UpdateResponse{
        private Long id;
        UpdateResponse(Long id){ this.id = id;}
    }

    @DeleteMapping("tasknotice/{id}")
    public void deleteTasknotice(@PathVariable("id") Long id) {
        tasknoticeService.delete(id);
    }

    @Data
    static class ttDTO{   // Tasknotice_Tag(객체에서)의 id만 가져오도록
        private Long id;
        public ttDTO(Tasknotice_Tag tasknotice_tag){
            id = tasknotice_tag.getId();
        }
    }

    @GetMapping("tasknotice")
    public Result ReadTasknotice() {
        List<Tasknotice> tasknotices = tasknoticeService.findAll();
        List<TasknoticeDto> collect = tasknotices.stream()
                .map(t -> new TasknoticeDto(t))
                .collect(Collectors.toList());
        return new Result(collect);
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
        private Long like;

        public TasknoticeDto(Tasknotice tasknotice) {
            id = tasknotice.getId();
            member = tasknotice.getMember();
            date = tasknotice.getDate();
            deadline = tasknotice.getDeadline();
            //updatetime = tasknotice.getUpdateTime();
            target = tasknotice.getTarget();
            image = tasknotice.getImage();
            title = tasknotice.getTitle();
            explanation = tasknotice.getExplanation();
            like = tasknotice.getLike();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    //FE파트 tasknotice조회
    @GetMapping("tasknotice/fe")
    public Result ReadTasknoticeFe() {
        List<Tasknotice> tasknotices = tasknoticeService.findFe();
        List<TasknoticeDto> collect = tasknotices.stream()
                .map(t -> new TasknoticeDto(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    // BE파트 tasknotice조회
    @GetMapping("tasknotice/be")
    public Result ReadTasknoticeBe() {
        List<Tasknotice> tasknotices = tasknoticeService.findBe();
        List<TasknoticeDto> collect = tasknotices.stream()
                .map(t -> new TasknoticeDto(t))
                .collect(Collectors.toList());
        return new Result(collect);
    }


}
