package haja.Project.api;

import haja.Project.domain.Tag;
import haja.Project.repository.TagRepository;
import haja.Project.service.TagService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService tagService;

    @PostMapping("create/tag")  //태그 생성
    public CreateTagResponse createTagResponse(@RequestBody @Valid CreateTagRequest request){
        Tag tag = new Tag();
        tag.setName(request.getName());
        Long id = tagService.save(tag);

        return new CreateTagResponse(id);
    }

    @Data
    static class CreateTagRequest{
        private String name;
    }
    @Data
    static class CreateTagResponse{
        private Long id;
        public CreateTagResponse(Long id){
            this.id = id;
        }
    }

}
