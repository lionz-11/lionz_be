package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_link", columnDefinition = "TEXT")
    private String link;

    @Column(name = "task_explanation", columnDefinition = "TEXT")
    private String explanation;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "task_date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "task_member_id") //user_id
    private Member member;

    @ManyToOne
    @JoinColumn(name = "task_tasknotice_id")//tasknotice_id
    private Tasknotice tasknotice;
}
