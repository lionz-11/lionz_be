package haja.Project.api;

import haja.Project.domain.Tag;
import haja.Project.repository.TagRepository;
import haja.Project.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag")
public class TagApiController {

    private final TagService tagService;


    @Operation(summary = "태그 생성", description = "velog처럼 태그 입력하고 엔터누르면 생성되는 것을 생각함, 다른 코드 내에서 태그 생성하게 해놔서 쓸 일 없을듯? -김예은-")
    @PostMapping("tag")  //태그 생성
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
