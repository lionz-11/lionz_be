package haja.Project.repository;

import haja.Project.domain.Task;
import haja.Project.util.SecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Task task){
        em.persist(task);
    }

    public Task findOne(Long id){
        return em.find(Task.class,id);
    }

    public List<Task> findAll(){
        return em.createQuery("select t from Task t", Task.class).getResultList();
    }

    public Optional<Task> findSubmit(Long tasknotice_id) {
        Long member_id = SecurityUtil.getCurrentMemberId();
        List<Task> task = em.createQuery("select t from Task t where t.tasknotice.id = :tasknotice_id and t.member.id = :member_id", Task.class)
                .setParameter("tasknotice_id", tasknotice_id)
                .setParameter("member_id", member_id)
                .getResultList();
        return task.stream().findAny();
    }

    public void delete(Long id){
        em.createQuery("delete from Task t where t.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }
}
