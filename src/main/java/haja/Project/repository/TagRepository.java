package haja.Project.repository;

import haja.Project.domain.Tag;
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

    public Optional<Tag> findByName(String name) {
        List<Tag> tags = em.createQuery("select t from Tag t where t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList();
        return tags.stream().findAny();
    }
}
