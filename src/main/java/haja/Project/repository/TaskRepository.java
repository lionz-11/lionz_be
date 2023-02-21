package haja.Project.repository;

import haja.Project.domain.Task;
import haja.Project.domain.Tasknotice;
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

    //explanation으로 검색 (AllSearch에 사용됨)
    public List<Task> findByWord(String word){  //and가 아니라 or!
        return em.createQuery("select t from Task t where t.explanation Like :word", Task.class)
                .setParameter("word",word)
                .getResultList();
    }
    public void delete(Long id){
        em.createQuery("delete from Task t where t.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }
}
