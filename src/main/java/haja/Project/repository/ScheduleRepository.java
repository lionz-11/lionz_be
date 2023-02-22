package haja.Project.repository;

import haja.Project.domain.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class ScheduleRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Schedule schedule){
        em.persist(schedule);
    }

    public Schedule findOne(Long id){
        return em.find(Schedule.class,id);
    }

    public List<Schedule> findAll(){
        return em.createQuery("select s from Schedule s", Schedule.class).getResultList();
    }

    public void delete(Long id){
        em.createQuery("delete  from Schedule s where s.id = :id")
                .setParameter("id",id)
                .executeUpdate();
    //delete s from Schedule ~ 이 아님
    }
}
