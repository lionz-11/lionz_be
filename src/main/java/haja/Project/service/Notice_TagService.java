package haja.Project.service;

import haja.Project.domain.Notice_Tag;
import haja.Project.repository.Notice_TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Notice_TagService {

    private final Notice_TagRepository notice_tagRepository;

    @Transactional
    public Long save(Notice_Tag notice_tag) {
        notice_tagRepository.save(notice_tag);
        return notice_tag.getId();
    }

    @Transactional
    public int deleteByNoticeId(Long notice_id) {
        return notice_tagRepository.deleteByNoticeId(notice_id);
    }
}
