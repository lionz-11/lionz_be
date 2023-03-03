package haja.Project.api;

import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.Authority;
import haja.Project.domain.Member;
import haja.Project.domain.Part;
import haja.Project.service.MemberService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PutMapping
    public MemberDto updateMember(@RequestBody @Valid MemberUpdateRequest request) {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        memberService.update(SecurityUtil.getCurrentMemberId(), request.phone_num, request.part, request.comment, request.major, request.student_id);
        return new MemberDto(member);
    }

    @GetMapping
    public MemberDto MemberInfo() {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        return new MemberDto(member);
    }

    @GetMapping("/email/{email}")
    public MemberDto findMemberInfoByEmail(@PathVariable("email") String email) {
        return new MemberDto(memberService.findByEmail(email).get());
    }

    @GetMapping("/{id}")
    public MemberDto findMemberInfoById(@PathVariable("id") Long id) {
        return new MemberDto(memberService.findById(id).get());
    }


    @Data
    static class MemberUpdateRequest {
        String phone_num;
        Part part;
        String comment;
        String major;
        String student_id;

    }
    @Data
    static class MemberDto {
        Long id;
        String email;
        Authority authority;
        String phone_num;
        Part part;
        String comment;
        String major;
        String student_id;

        public MemberDto(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.authority = member.getAuthority();
            this.phone_num = member.getPhone_num();
            this.part = member.getPart();
            this.comment = member.getComment();
            this.major = member.getMajor();
            this.student_id = member.getStudent_id();
        }
    }
}
