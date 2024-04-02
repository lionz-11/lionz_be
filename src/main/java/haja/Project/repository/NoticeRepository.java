package haja.Project.repository;

import haja.Project.domain.Notice;
import haja.Project.domain.Part;
import haja.Project.domain.Tasknotice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NoticeRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Notice notice) { em.persist(notice); }

    public List<Notice> findAll() {
        return em.createQuery("select n from Notice n", Notice.class)
                .getResultList();
    }

    public Notice findById(Long id) {
        return em.find(Notice.class, id);
    }

    public List<Notice> findByTarget(String target) {
        Part part = Part.valueOf(target);
        return em.createQuery("select n from Notice n where n.target = :part", Notice.class)
                .setParameter("part", part)
                .getResultList();
    }

    public List<Notice> findByWord(String word){  //and가 아니라 or!
        return em.createQuery("select n from Notice n where n.title like :word or n.explanation like :word", Notice.class)
                .setParameter("word","%"+word+"%")
                .getResultList();
    }

    public void update(Long id, String title, String explanation, LocalDateTime deadline) {
        Notice notice = findById(id);
        notice.setTitle(title);
        notice.setExplanation(explanation);
        notice.setDeadline(deadline);
        notice.setDate(LocalDateTime.now());
    }

    public void delete(Long id) {
        em.createQuery(
                "delete from Notice n where n.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

}
