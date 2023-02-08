package haja.Project.service;

import haja.Project.domain.Tag;
import haja.Project.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Long save(Tag tag){
        tagRepository.save(tag);
        return tag.getId();
    }

    public Tag findOne(Long id){
        return tagRepository.findOne(id);
    }
}
