package haja.Project.repository;

import haja.Project.domain.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

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
}
