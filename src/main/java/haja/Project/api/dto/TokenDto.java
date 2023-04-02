package haja.Project.api.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    private String id;
    private Integer count;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime accessTokenExpiresIn;
}
