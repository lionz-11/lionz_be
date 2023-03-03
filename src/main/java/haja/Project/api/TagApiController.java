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

    @io.swagger.v3.oas.annotations.tags.Tag(name = "태그 생성",
            description = "버튼은 아니지만 velog처럼 태그 입력하고 엔터누르면 생성되는 것을 생각함" +
                    "->프론트분들이 엔터누를때 이 로직(url)이 실행되게끔 해주면 될 듯")
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
