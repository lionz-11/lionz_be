package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Tasknotice {
    // 태그 넣어야 함!


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tasknotice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")//
    private User user;

    @Column(name = "tasknotice_date")
    private LocalDateTime date;

    @Column(name = "tasknotice_updatetime")
    private LocalDateTime updateTime;

    @Column(name = "tasknotice_deadline")
    private LocalDateTime deadline;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tasknotice_target")
    private Part target;

    @Column(name = "tasknotice_image")
    private File image; // String->File

    @Column(name = "tasknotice_title")
    private String title;

    @Column(name = "tasknotice_explanation")
    private String explanation;

    @Column(name = "tasknotice_like")
    private Long like;
}
