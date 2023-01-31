package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Notice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notice_title")
    private String title;

    @Column(name = "notice_explanation")
    private String explanation;

    @Column(name = "notice_date")
    private LocalDate date;

    @Column(name = "notice_deadline")
    private LocalDateTime deadline;

    @Column(name = "notice_like")
    private Long like; //좋아요


}
