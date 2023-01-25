package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) //seq테이블 안생기게끔
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_num")
    private String phone_num;

    @Column(name = "part")
    private String part;

    @Column(name = "comment")
    private String comment;

    @Column(name = "major")
    private String major;

    @Column(name = "student_id")
    private String student_id;

    @Column(name = "is_staff")
    private boolean is_staff;
}
