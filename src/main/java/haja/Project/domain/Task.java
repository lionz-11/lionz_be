package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_link")
    private String link;

    @Column(name = "task_explanation")
    private String explanation;

    @Column(name = "task_date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "task_member_id") //user_id
    private Member member;

    @ManyToOne
    @JoinColumn(name = "task_tasknotice_id")//tasknotice_id
    private Tasknotice tasknotice;
}
