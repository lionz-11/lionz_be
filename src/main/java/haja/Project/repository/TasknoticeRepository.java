package haja.Project.repository;

import haja.Project.domain.Tasknotice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TasknoticeRepository {

    @PersistenceContext
    private EntityManager em;

    //저장
    public void save(Tasknotice tasknotice){ em.persist(tasknotice); }

    //단건조회
    public Tasknotice findOne(Long id){
        return em.find(Tasknotice.class, id);
    }

    //모든 tasknotice 조회
    public List<Tasknotice> findAll(){
        return em.createQuery("select m from Tasknotice m", Tasknotice.class).getResultList();
    }
    //이름으로 검색
    public List<Tasknotice> findByName(String name){
        return em.createQuery("select m from Tasknotice m where m.name = :name", Tasknotice.class)
                .setParameter("name",name)
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
