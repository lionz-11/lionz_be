package haja.Project.service;

import haja.Project.domain.Part;
import haja.Project.domain.Tasknotice;
import haja.Project.repository.TasknoticeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TasknoticeService {

    private final TasknoticeRepository tasknoticeRepository;

    @Transactional
    public Long join(Tasknotice tasknotice) {
        //tasknotice.setUser(현재 로그인한 사용자 정보)
        tasknotice.setDate(LocalDateTime.now());
        tasknoticeRepository.save(tasknotice);
        return tasknotice.getId();
    }

    public List<Tasknotice> findTasknotice() {
        return tasknoticeRepository.findAll();
    }

    public Tasknotice findOne(Long id) {
        return tasknoticeRepository.findOne(id);
    }

    @Transactional // 데드라인 json 자료형 찾아보고 수정
    public void update(Long id, Part target, File image, String title, String explanation) {
        Tasknotice tasknotice = tasknoticeRepository.findOne(id);
        tasknotice.setUpdateTime(LocalDateTime.now());
        //tasknotice.setDeadline(LocalDateTime.now()); // 수정필요!!
        tasknotice.setTarget(target);
        tasknotice.setImage(image);
        tasknotice.setTitle(title);
        tasknotice.setExplanation(explanation);

    }

    @Transactional
    public void delete(Long id) {
        tasknoticeRepository.deleteTasknotice(id);
    }
}
