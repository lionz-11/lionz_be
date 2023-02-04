package haja.Project.service;

import haja.Project.domain.Tasknotice;
import haja.Project.repository.TasknoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
