package haja.Project.repository;

import haja.Project.domain.Tag;
import haja.Project.domain.Tasknotice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Tag tag){
        em.persist(tag);
    }

    public Tag findOne(Long id){
        return em.find(Tag.class,id);
    }

    //tag name으로 검색 (AllSearch에 사용됨)
    public List<Tag> findByWord(String word){  //and가 아니라 or!
        return em.createQuery("select t from Tag t where t.name Like :word" , Tag.class)
                .setParameter("word",word)
                .getResultList();
    }

    public Optional<Tag> findByName(String name) {
        List<Tag> tags = em.createQuery("select t from Tag t where t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList();
        return tags.stream().findAny();
    }
}
