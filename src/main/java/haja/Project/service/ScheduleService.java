package haja.Project.service;

import haja.Project.domain.Schedule;
import haja.Project.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long save(Schedule schedule){
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    public Schedule findOne(Long id){
        return scheduleRepository.findOne(id);
    }

    public List<Schedule> findAll(){
        return scheduleRepository.findAll();
    }

    @Transactional
    public void delete(Long id){
        scheduleRepository.delete(id);
    }
}
