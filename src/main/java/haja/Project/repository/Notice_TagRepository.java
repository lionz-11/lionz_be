package haja.Project.repository;

import haja.Project.domain.Notice_Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class Notice_TagRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Notice_Tag notice_tag) { em.persist(notice_tag); }

    public int deleteByNoticeId(Long id) {
        return em.createQuery("delete from Notice_Tag n where n.notice.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
