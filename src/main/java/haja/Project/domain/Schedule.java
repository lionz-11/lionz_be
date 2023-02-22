package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_title")
    private String title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "schedule_category")
    private Category category;

    @Column(name = "schedule_startdate")
    private LocalDateTime startdate;

    @Column(name = "schedule_enddate")
    private LocalDateTime enddate;
}
