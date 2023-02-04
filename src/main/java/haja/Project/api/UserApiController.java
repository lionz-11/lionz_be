package haja.Project.api;

import haja.Project.domain.User;
import haja.Project.service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("user/join")
    public CreateUserResponse saveUser(@RequestBody @Valid CreateUserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());

        Long id = userService.join(user);
        return new CreateUserResponse(id);
    }

    @Data
    static class CreateUserRequest{
        private String email;
    }

    @Data
    static class CreateUserResponse{
        private Long id;
        public CreateUserResponse(Long id){ this.id = id;}
    }

}
