package haja.Project.repository;

import haja.Project.domain.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public void delete(Long id){
        em.createQuery("delete from Task t where t.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }
}
