package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name")
    private String name;

    /*  // Notice_Tag와 양방향 매핑 시
    @OneToMany(mappedBy = "tag")
    private List<Notice_Tag> noticetag = new ArrayList<>();
    */

}
