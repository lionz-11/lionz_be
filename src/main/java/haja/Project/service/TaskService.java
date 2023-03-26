package haja.Project.service;

import haja.Project.domain.Task;
import haja.Project.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<Task> findByTasknotice(Long id) {
        return taskRepository.findByTasknotice(id);
    }
    public Task findOne(Long id){
        return taskRepository.findOne(id);
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public List<Task> findByMember(Long id) {
        return taskRepository.findByMember(id);
    }

    public boolean isSubmit(Long tasknotice_id) {
        Optional<Task> task = taskRepository.findSubmit(tasknotice_id);
        if (task.isEmpty()) return false;
        else return true;
    }
    @Transactional
    public void delete(Long id){
        taskRepository.delete(id);
    }

    @Transactional
    public void deleteByTasknotice(Long id) { taskRepository.deleteByTasknotice(id); }

    public List<Task> findByWord(String word){
        return taskRepository.findByWord(word);
    }

}
