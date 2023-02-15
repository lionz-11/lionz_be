package haja.Project.service;

import haja.Project.domain.Tag;
import haja.Project.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Tag findByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        if (tag.isEmpty()) return null;
        else return tag.get();
    }
}
