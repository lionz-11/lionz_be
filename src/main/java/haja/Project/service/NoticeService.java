package haja.Project.service;

import haja.Project.domain.Notice;
import haja.Project.domain.Tasknotice;
import haja.Project.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Long save(Notice notice) {
        noticeRepository.save(notice);
        return notice.getId();
    }

    public Notice findById(Long id) { return noticeRepository.findById(id); }

    public List<Notice> findAll() { return noticeRepository.findAll(); }

    public List<Notice> findByTarget(String target) { return noticeRepository.findByTarget(target); }

    public List<Notice> findByWord(String word){
        return noticeRepository.findByWord(word);
    }

    @Transactional
    public void update(Long id, String title, String explanation, LocalDateTime deadline) {
        noticeRepository.update(id, title, explanation, deadline);
    }

    @Transactional
    public void delete(Long id) { noticeRepository.delete(id); }
}
