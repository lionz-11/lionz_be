package haja.Project.api;

import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.api.dto.TokenDto;
import haja.Project.api.dto.TokenRequestDto;
import haja.Project.domain.Member;
import haja.Project.domain.RefreshToken;
import haja.Project.repository.RefreshTokenRepository;
import haja.Project.service.AuthService;
import haja.Project.service.MemberService;
import haja.Project.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody accessDTO accessDTO) {
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setAccessToken(accessDTO.accessToken);
        tokenRequestDto.setRefreshToken(refreshTokenRepository.findByAccessToken(accessDTO.getAccessToken()).get().getValue());
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @Data
    @NoArgsConstructor
    static class accessDTO {
        String accessToken;
    }
}
