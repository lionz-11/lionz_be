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
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// 사진을 100kb로 줄이자

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
    public ResponseEntity<Void> updateMemberImage(@RequestBody @Valid MultipartFile file) throws IOException {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        Date date = new Date();

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String file_name = date.getTime() + member.getStudent_id() + file.getOriginalFilename();
        //String img_path = "C:\\Users\\kjk87\\Desktop\\img\\" + file_name;
        String img_path = "/home/img/" + file_name;
        //String img_link = "http://localhost:8080/member/img/" + file_name;
        String img_link = "https://lionz.kro.kr/member/img/" + file_name;
        File dest = new File(img_path);

        // 이미지 용량 제한
        BufferedImage bufferedImage = Scalr.resize(ImageIO.read(file.getInputStream()), 1000, 1000, Scalr.OP_ANTIALIAS);
        ImageIO.write(bufferedImage, "jpg", dest);

        memberService.setImage(member, img_link, img_path);

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

    @Operation(summary = "멤버 프로필 조회", description = "member 조회 json - image에 링크가 들어있어서.. 직접 쓰실 일은 없을 거 같습니다")
    @GetMapping(value = "/img/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("image") String image) throws IOException {
        InputStream inputStream = new FileInputStream("/home/img/" + image);
        byte[] bytes = inputStream.readAllBytes();
        inputStream.close();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(Paths.get("/home/img/" + image)));
        return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
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
        String image;

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
            this.image = member.getImage();
        }
    }
}
