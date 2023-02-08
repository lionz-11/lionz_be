package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Tag {
    //글 한개는 여러개의 태그를 포함할 수 있다
    //한개의 태그는 여러개의 글에 포함될 수 있다
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name")
    private String name;

    /*  // Notice_Tag와 양방향 매핑 시
    @OneToMany(mappedBy = "tag")
    private List<Notice_Tag> noticetags = new ArrayList<>();
    */

}
