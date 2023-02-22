package haja.Project.service;


import haja.Project.domain.Task_Tag;
import haja.Project.repository.Task_TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Task_TagService {

    private final Task_TagRepository task_tagRepository;

    @Transactional
    public Long save(Task_Tag task_tag){
        task_tagRepository.save(task_tag);
        return task_tag.getId();
    }

    public Task_Tag findOne(Long id){
        return task_tagRepository.findOne(id);
    }

    public List<Task_Tag> findByTaskId(Long id){
        return task_tagRepository.findByTaskId(id);
    }

    public List<Task_Tag> findByTagName(String name){
        return task_tagRepository.findByTagName(name);
    }

    @Transactional
    public void delete(Task_Tag task_tag){
        task_tagRepository.delete(task_tag);
    }

    @Transactional
    public void deleteByTaskId(Long id) {
        task_tagRepository.deleteByTaskId(id);
    }

}
