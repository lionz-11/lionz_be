package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@DynamicInsert
@Table(name = "member")
@Entity
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "phone_num")
    private String phone_num;

    @Enumerated(EnumType.STRING) //이거 안넣으니까 DB에서 수정이 안되었다는 슬픈 사실이..
    @Column(name = "part")
    private Part part;

    @Column(name = "comment")
    private String comment;

    @Column(name = "major")
    private String major;

    @Column(name = "student_id")
    private String student_id;

    @Embedded
    private Image image;

    @Column(name = "member_count",nullable = false)
    @ColumnDefault("0")
    private Integer count;

    @Column(name = "Token_count")
    private LocalDateTime accessTokenExpiresIn;

    @Builder
    public Member(Long id, String email, String password, Authority authority, String name, String phone_num, Part part, String comment, String major, String student_id, Image image) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.name = name;
        this.phone_num = phone_num;
        this.part = part;
        this.comment = comment;
        this.major = major;
        this.student_id = student_id;
        this.image = image;
    }
/*

    @Builder
    public Member(String email, String password, Authority authority) {
        this.email = email;
        this.password = password;
        this.authority = authority;
    }
*/
}