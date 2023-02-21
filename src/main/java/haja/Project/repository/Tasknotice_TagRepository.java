package haja.Project.repository;

import haja.Project.domain.Tasknotice_Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Tasknotice_TagRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Tasknotice_Tag tasknotice_tag){
        em.persist(tasknotice_tag);
    }

    public Tasknotice_Tag findOne(Long id){
        return em.find(Tasknotice_Tag.class,id);
    }
    //여기에 findByTasknoticeId랑 findByTagId 메서드 추가

    public void delete(Tasknotice_Tag tasknotice_tag){
        em.remove(tasknotice_tag);
    }

    public List<Tasknotice_Tag> findByTasknoticeId(Long id){
       return em.createQuery("select tt from Tasknotice_Tag tt where tt.tasknotice.id = :id", Tasknotice_Tag.class)
                .setParameter("id",id)
                .getResultList();
    }

    //저기 쿼리문에서 줄바꿀 때 공백(space) 줘야 에러 안나더라  //  tg " 마냥
    public List<Tasknotice_Tag> findByTagName(String name){
        return em.createQuery("select tt from Tasknotice_Tag tt join tt.tasknotice tn join tt.tag tg "
                        +"where tg.name Like :name and tt.tag.id = tg.id and tt.tasknotice.id = tn.id",Tasknotice_Tag.class)
                .setParameter("name",name)
                .getResultList();
    }

    public void deleteByTasknoticeId(Long id) {
        em.createQuery("delete from Tasknotice_Tag t where t.tasknotice.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }


}
