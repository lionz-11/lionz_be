package haja.Project.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Tasknotice_Tag { //게시글(Tasknotice)에 태그등록이라는 action으로 보면될듯 // order마냥
    // 그럼 태그로 검색했을때 그 태그를 사용한 글들을 모두 나오도록 할 수 있고
    // Tasknotice create url로 post가 오면 if else문 으로
    // if 태그 존재하면 이거 else 태그없으면 이거 이런식으로 만들면 되겠다
    // 태그쓰고 입력버튼(url)누르면 tag객체 생성되고 (createTag) //

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tasknotce_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasknotice_id")
    private Tasknotice tasknotice;

    //여기에 tag객체 여러개가 들어가야하는데-> 그럴 필요없이 Tasknotice_Tag객체를 계속만들면됨
    // 게시글id 1에 tag id 1들어가고 tag id 2도 들어가고 이런식으로
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
