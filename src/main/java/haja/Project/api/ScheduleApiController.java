package haja.Project.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import haja.Project.domain.Category;
import haja.Project.domain.Schedule;
import haja.Project.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController  //그냥 @Controller로 하면 [Circular view path~] 에러 발생함
@RequiredArgsConstructor
public class ScheduleApiController {

    private final ScheduleService scheduleService;

    //일정 생성
    @Tag(name = "일정 생성 버튼")
    @PostMapping("schedule")
    public CreateScheduleResponse createScheduleResponse(@RequestBody @Valid CreateScheduleRequest request){
        Schedule schedule = new Schedule();
        schedule.setTitle(request.getTitle());
        schedule.setCategory(request.getCategory());
        schedule.setStartdate(request.getStartdate());
        schedule.setEnddate(request.getEnddate());

        Long id = scheduleService.save(schedule);

        return new CreateScheduleResponse(id);
    }

    @Data
    static class CreateScheduleRequest{

        private String title;
        private Category category;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startdate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime enddate;
    }
    @Data
    static class CreateScheduleResponse{
        private Long id;
        CreateScheduleResponse(Long id){
            this.id = id;
        }
    }

    //일정 all 조회
    @Tag(name = "모든 일정 조회")
    @GetMapping("schedule")
    public scheduleResult AllSchedule(){
        List<Schedule> schedules = scheduleService.findAll();
        List<ScheduleDto> sdt = schedules.stream()
                .map(t -> new ScheduleDto(t))
                .collect(Collectors.toList());

        return new scheduleResult(sdt);  // 아래 scheduleResult에 @AllArgsConstructor없으면 에러남
    }
    @Data
    @AllArgsConstructor
    static class scheduleResult<T>{
        private T schedule;
    }

    //일정 수정 (하러가기) 버튼 -> 이미 만들어놓은 일정 그대로 가져와서 보여주기
    @Tag(name = "일정 수정 (하러가기)",description = "수정 완료 버튼이랑 다름/ 작성해뒀던 일정 그대로 가져와서 보여주고 수정 할 부분만 수정하는 것을" +
            "생각함")
    @GetMapping("button/schedule/{id}")
    public ScheduleDto scheduleDto(@PathVariable("id") Long id){

        ScheduleDto sc = new ScheduleDto(scheduleService.findOne(id));
        return sc;

    }

    @Data
    @AllArgsConstructor
    static class ScheduleDto{
        private Long id;
        private String title;
        private Category category;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startdate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime enddate;
        ScheduleDto(Schedule schedule){
            id = schedule.getId();
            title = schedule.getTitle();
            startdate = schedule.getStartdate();
            enddate = schedule.getEnddate();
        }

    }

    //일정 수정 완료
    @Tag(name = "일정 수정 완료 버튼",description = "수정 할 부분 수정하고 수정완료 버튼 누르면 저장됨")
    @PutMapping("schedule/{id}")
    public UpdateScheduleResponse updateScheduleResponse(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateScheduleRequest request){

        Schedule schedule;
        schedule = scheduleService.findOne(id);
        schedule.setTitle(request.getTitle());
        schedule.setCategory(request.getCategory());
        schedule.setStartdate(request.getStartdate());
        schedule.setEnddate(request.getEnddate());
        Long sche_id = scheduleService.save(schedule);
        return new UpdateScheduleResponse(sche_id);
    }


    @Data
    static class UpdateScheduleRequest{

        private String title;
        private Category category;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startdate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime enddate;
    }
    @Data
    static class UpdateScheduleResponse{
        private Long id;
        UpdateScheduleResponse(Long id){
            this.id = id;
        }
    }

    //삭제
    @Tag(name = "일정 삭제 버튼")
    @DeleteMapping("schedule/{id}")
    public void deleteSchedule(@PathVariable("id") Long id){
        scheduleService.delete(id);
    }
}
