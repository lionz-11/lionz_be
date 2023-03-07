package haja.Project.api;

import com.sun.net.httpserver.Authenticator;
import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.Authority;
import haja.Project.domain.Member;
import haja.Project.domain.Part;
import haja.Project.service.MemberService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "멤버 수정")
    @PutMapping
    public MemberDto updateMember(@RequestBody @Valid MemberUpdateRequest request) {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        memberService.update(SecurityUtil.getCurrentMemberId(), request.phone_num, request.part, request.comment, request.major, request.student_id);
        return new MemberDto(member);
    }

    @Operation(summary = "멤버 프로필 업로드")
    @PostMapping("/img")
    public ResponseEntity<Void> updateMemberimage(@RequestBody @Valid MultipartFile file) {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        Date date = new Date();
        String file_name = date.getTime() + member.getStudent_id();

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        File dest = new File("/home/img/" + file_name + file.getOriginalFilename());
        try{
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }
    @Operation(summary = "로그인 중인 멤버 조회")
    @GetMapping
    public MemberDto MemberInfo() {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        return new MemberDto(member);
    }

    @Operation(summary = "id로 멤버 조회")
    @GetMapping("/{id}")
    public MemberDto findMemberInfoById(@PathVariable("id") Long id) {
        return new MemberDto(memberService.findById(id).get());
    }

    @Operation(summary = "전체 멤버 조회")
    @GetMapping("/all")
    public Result findAllMember() {
        List<Member> members = memberService.findAll();
        List<MemberDto> memberResult = members.stream()
                .map(member -> new MemberDto(member))
                .collect(Collectors.toList());
        return new Result(memberResult);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
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
        String name;
        String comment;
        String major;
        String student_id;

        public MemberDto(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.authority = member.getAuthority();
            this.phone_num = member.getPhone_num();
            this.part = member.getPart();
            this.name = member.getName();
            this.comment = member.getComment();
            this.major = member.getMajor();
            this.student_id = member.getStudent_id();
        }
    }
}
