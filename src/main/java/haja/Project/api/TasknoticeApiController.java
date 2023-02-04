package haja.Project.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import haja.Project.domain.Part;
import haja.Project.domain.Tasknotice;
import haja.Project.domain.User;
import haja.Project.repository.UserRepository;
import haja.Project.service.TasknoticeService;
import haja.Project.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TasknoticeApiController {

    private final TasknoticeService tasknoticeService;
    private final UserService userService;

    //과제공지글 생성
    //어차피 request.뭐시기 해서 일일히 다 넣어줘야해서 service패키지에 메서드 안만들었음
    @PostMapping("tasknotice/create")
    public CreateTasknoticeResponse createTasknotice(@RequestBody @Valid CreateTasknoticeRequest request){
        Tasknotice tasknotice = new Tasknotice();

        tasknotice.setUser(userService.findOne(request.getUser_id()));
        tasknotice.setDate(LocalDateTime.now()); //생성시점
        tasknotice.setDeadline(request.getDeadline()); // postman으로 날짜받는거
        tasknotice.setTarget(request.getTarget());
        tasknotice.setTitle(request.getTitle());
        tasknotice.setExplanation(request.getExplanation());

        Long id = tasknoticeService.save(tasknotice);
        return new CreateTasknoticeResponse(id);  // new 조심
    }

    @Data
    static class CreateTasknoticeRequest{
        //private User user; //원래 이렇게 객체로 받고싶었는데 postman으로 그게 안돼서
        private Long user_id;  // "user_id":"1" 이렇게 받고 userService에 있는 findOne으로 user객체불러와서 set해주는방식

        @CreatedDate
        private LocalDateTime date;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //데드라인을 어떤형식으로 받을지 명시
        private LocalDateTime deadline;
        private Part target;
        //private File image; // 이건 나중에
        private String title;
        private String explanation;
    }

    @Data
    static class CreateTasknoticeResponse{
        private Long id;
        public CreateTasknoticeResponse(Long id){
            this.id = id;
        }
    }
}
