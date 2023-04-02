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

    public List<Task> findByMember(Long id) {
        return em.createQuery("select t from Task t where t.member.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
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

    public List<Task> findByTasknotice(Long id) {
        return em.createQuery("select t from Task t where t.tasknotice.id = :id")
                .setParameter("id", id)
                .getResultList();
    }

    public void delete(Long id){
        em.createQuery("delete from Task t where t.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }

    public void deleteByTasknotice(Long id) {
        em.createQuery("delete from Task t where t.tasknotice.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    //explanation으로 검색 (AllSearch에 사용됨)
    public List<Task> findByWord(String word){  //and가 아니라 or!
        return em.createQuery("select t from Task t where t.explanation Like :word", Task.class)
                .setParameter("word","%"+word+"%")
                .getResultList();
    }

}
