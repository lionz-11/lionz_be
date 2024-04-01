package haja.Project.api;

import com.sun.net.httpserver.Authenticator;
import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.*;
import haja.Project.service.MemberService;
import haja.Project.service.TasknoticeService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// 사진을 100kb로 줄이자

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "Member")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "멤버 코멘트 수정")
    @PutMapping("comment")
    public MemberDto updateMemberComment(@RequestBody @Valid MemberUpdateCommentRequest request) {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        memberService.updateComment(SecurityUtil.getCurrentMemberId(), request.getComment());
        return new MemberDto(member);
    }
    @Operation(summary = "멤버 비밀번호 수정")
    @PutMapping("password")
    public MemberDto updateMemberPassword(@RequestBody @Valid MemberUpdatePasswordRequest request) {
        if(request.getPassword() != null) {
            Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
            memberService.updatePassword(SecurityUtil.getCurrentMemberId(),
                    passwordEncoder.encode(request.getPassword()));
            return new MemberDto(member);
        }
        else
            return null;
    }


    @Operation(summary = "멤버 프로필 업로드")
    @PostMapping(value = "/img", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMemberImage(@RequestBody @Valid MultipartFile file) throws IOException {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();
        Date date = new Date();

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String file_name = date.getTime() + file.getOriginalFilename();
        //String img_path = "C:\\Users\\kjk87\\Desktop\\img\\" + file_name;
        String img_path = "/home/img/" + file_name;
        //String img_link = "http://localhost:8080/member/img/" + file_name;
        String img_link = "https://lionz.kro.kr/member/img/" + file_name;
        File dest = new File(img_path);

        // 이미지 용량 제한
        String format = file_name.substring(file_name.lastIndexOf(".") + 1);
        BufferedImage bufferedImage = Scalr.resize(ImageIO.read(file.getInputStream()), 1000, 1000, Scalr.OP_ANTIALIAS);
        ImageIO.write(bufferedImage, format, dest);

        Image image = new Image(img_link, file_name, img_path);
        memberService.setImage(member, image);


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

        @Operation(summary = "멤버 id로 프로필 조회")
        @GetMapping(value = "/img/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
        public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {
        //String path = "C:\\Users\\kjk87\\Desktop\\img\\";
            String path = "/home/img/";
            InputStream inputStream = new FileInputStream(path + name);
            byte[] bytes = inputStream.readAllBytes();
        inputStream.close();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(Paths.get(path + name)));
        return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
    }
//에휴ㅅㅂ
    @Operation(summary = "현재 로그인한 멤버의 프로필 삭제")
    @DeleteMapping(value = "/img")
    public void deleteImage() {
        Member member = memberService.findById(SecurityUtil.getCurrentMemberId()).get();

        // 기본 이미지 삭제 불가
        if (member.getImage().img_name == "DefaultProfile.png") {
            return;
        }

        Image image = member.getImage();
        File file = new File(image.img_path);
        if(file.exists()) file.delete();

        memberService.deleteImage(member);



    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


    @Data
    static class MemberUpdateCommentRequest {
        String comment;
    }
    @Data
    static class MemberUpdatePasswordRequest {
        String password;
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
        Image image;
        Long accessTokenExpiresIn;

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
            if (member.getAccessTokenExpiresIn()!=null) {
                this.accessTokenExpiresIn = Duration.between(LocalDateTime.now(), member.getAccessTokenExpiresIn()).toMinutes();
            } else this.accessTokenExpiresIn = null;
        }
    }
}
