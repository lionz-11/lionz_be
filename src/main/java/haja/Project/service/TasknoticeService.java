package haja.Project.service;

import haja.Project.domain.Tasknotice;
import haja.Project.repository.TasknoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TasknoticeService {

    private final TasknoticeRepository tasknoticeRepository;

    @Transactional
    public Long save(Tasknotice tasknotice){
        tasknoticeRepository.save(tasknotice);
        return tasknotice.getId();
    }

    public Tasknotice findOne(Long id){
        return tasknoticeRepository.findOne(id);
    }

    public List<Tasknotice> findAll() {
        return tasknoticeRepository.findAll();
    }

    @Transactional      // 과제공지글 수정
    public Long update(Tasknotice tasknotice){
        tasknoticeRepository.save(tasknotice);
        return tasknotice.getId();
    }

    @Transactional
    public void delete(Long id) {
        tasknoticeRepository.deleteTasknotice(id);
    }
}
