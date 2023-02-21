package haja.Project.service;

import haja.Project.domain.Tasknotice_Tag;
import haja.Project.repository.Tasknotice_TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Tasknotice_TagService {

    private final Tasknotice_TagRepository tasknoticeTagRepository;

    @Transactional
    public Long save(Tasknotice_Tag tasknotice_tag){
        tasknoticeTagRepository.save(tasknotice_tag);
        return tasknotice_tag.getId();
    }

    public Tasknotice_Tag findOne(Long id){
        return tasknoticeTagRepository.findOne(id);
    }

    public List<Tasknotice_Tag> findByTasknoticeId(Long id){
        return tasknoticeTagRepository.findByTasknoticeId(id);
    }

    public List<Tasknotice_Tag> findByTagName(String name){
        return tasknoticeTagRepository.findByTagName(name);
    }
    @Transactional
    public void delete(Tasknotice_Tag tasknotice_tag){
        tasknoticeTagRepository.delete(tasknotice_tag);
    }

    @Transactional
    public void deleteByTasknoticeId(Long id) {
        tasknoticeTagRepository.deleteByTasknoticeId(id);
    }

}
