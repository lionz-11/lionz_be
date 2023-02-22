package haja.Project.repository;

import haja.Project.domain.Task;
import haja.Project.domain.Tasknotice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static haja.Project.domain.Part.FE;

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
        return em.createQuery("select t from Tasknotice t", Tasknotice.class).getResultList();
    }

    // FE파트 tasknotice 조회
    public List<Tasknotice> findFe(){
        return em.createQuery("select t from Tasknotice t where t.target = FE",Tasknotice.class).getResultList();
    }

    // BE파트 tasknotice 조회
    public List<Tasknotice> findBe(){
        return em.createQuery("select t from Tasknotice t where t.target = BE",Tasknotice.class).getResultList();
    }

    //이름으로 검색
    public List<Tasknotice> findByName(String name){
        return em.createQuery("select t from Tasknotice t where t.name = :name", Tasknotice.class)
                .setParameter("name",name)
                .getResultList();
    }

    //title or explanation으로 검색 (AllSearch에 사용됨)
    public List<Tasknotice> findByWord(String word){  //and가 아니라 or!
        return em.createQuery("select t from Tasknotice t where t.title Like :word or t.explanation Like :word", Tasknotice.class)
                .setParameter("word",word)
                .getResultList();
    }

    //삭제

    public void deleteTasknotice(Long id) {
        em.createQuery("delete from Tasknotice t where t.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }


}
