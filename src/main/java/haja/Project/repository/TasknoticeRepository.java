package haja.Project.repository;

import haja.Project.domain.Tasknotice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TasknoticeRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Tasknotice tasknotice) { em.persist(tasknotice); }

    public Tasknotice findOne(Long id) { return em.find(Tasknotice.class, id); }
    public List<Tasknotice> findAll() {
        return em.createQuery("select t from Tasknotice t", Tasknotice.class)
                .getResultList();
    }

    @Transactional
    public void deleteTasknotice(Long id) {
        em.createQuery(
                "delete from Tasknotice t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
