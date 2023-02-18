package haja.Project.service;

import haja.Project.domain.Task;
import haja.Project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Long save(Task task){
        taskRepository.save(task);
        return task.getId();
    }

    public Task findOne(Long id){
        return taskRepository.findOne(id);
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    @Transactional
    public void delete(Long id){
        taskRepository.delete(id);
    }
}
