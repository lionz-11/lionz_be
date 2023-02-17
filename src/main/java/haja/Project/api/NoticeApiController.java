package haja.Project.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import haja.Project.domain.*;
import haja.Project.service.MemberService;
import haja.Project.service.NoticeService;
import haja.Project.service.Notice_TagService;
import haja.Project.service.TagService;
import haja.Project.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NoticeApiController {
    private final NoticeService noticeService;
    private final MemberService memberService;
    private final TagService tagService;
    private final Notice_TagService notice_tagService;

    @PostMapping("notice")
    public NoticeResponse createNotice(@RequestBody @Valid NoticeRequest request) {
        Notice notice = new Notice();
        notice.setMember(memberService.findById(SecurityUtil.getCurrentMemberId()).get());
        notice.setTitle(request.title);
        notice.setExplanation(request.explanation);
        notice.setDate(LocalDate.now());
        notice.setDeadline(request.deadline);
        notice.setTarget(request.target);

        Long id = noticeService.save(notice);

        List<String> tags = request.tags;
        if (tags != null) {
            for (String tag_name : tags) {
                // 없는 태그면 Tag 생성하고 Notice_Tag 생성
                if (tagService.findByName(tag_name) == null) {
                    Tag tag = new Tag();
                    tag.setName(tag_name);
                    tagService.save(tag);

                    Notice_Tag notice_tag = new Notice_Tag();
                    notice_tag.setNotice(noticeService.findById(id));
                    notice_tag.setTag(tag);
                    notice_tagService.save(notice_tag);

                }
                // 있는 태그면 Notice_Tag만 생성
                else {
                    Notice_Tag notice_tag = new Notice_Tag();
                    notice_tag.setNotice(noticeService.findById(id));
                    notice_tag.setTag(tagService.findByName(tag_name));
                    notice_tagService.save(notice_tag);
                }
            }
        }
        return new NoticeResponse(id);

    }

    @GetMapping("notice")
    public Result readNotice() {
        List<Notice> notices = noticeService.findAll();
        List<NoticeDto> collect = notices.stream().map(n -> new NoticeDto(n))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @GetMapping("notice/{target}")
    public Result readNoticeByPart(@PathVariable("target") String target) {
        List<Notice> notices = noticeService.findByTarget(target);
        List<NoticeDto> collect = notices.stream().map(n -> new NoticeDto(n))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @PutMapping("notice/{id}")
    public NoticeResponse updateNotice(@PathVariable("id") Long id,
            @RequestBody @Valid NoticeRequest request) {
        noticeService.update(id, request.title, request.explanation, request.deadline);
        notice_tagService.deleteByNoticeId(id);

        // 중복 코드 없애면 좋을텐데,, 나중에 ㄱㄱ
        List<String> tags = request.tags;
        if (tags != null) {
            for (String tag_name : tags) {
                // 없는 태그면 Tag 생성하고 Notice_Tag 생성
                if (tagService.findByName(tag_name) == null) {
                    Tag tag = new Tag();
                    tag.setName(tag_name);
                    tagService.save(tag);

                    Notice_Tag notice_tag = new Notice_Tag();
                    notice_tag.setNotice(noticeService.findById(id));
                    notice_tag.setTag(tag);
                    notice_tagService.save(notice_tag);

                }
                // 있는 태그면 Notice_Tag만 생성
                else {
                    Notice_Tag notice_tag = new Notice_Tag();
                    notice_tag.setNotice(noticeService.findById(id));
                    notice_tag.setTag(tagService.findByName(tag_name));
                    notice_tagService.save(notice_tag);
                }
            }
        }

        return new NoticeResponse(id);
    }

    @DeleteMapping("notice/{id}")
    public void deleteNotice(@PathVariable("id") Long id) {
        notice_tagService.deleteByNoticeId(id);
        noticeService.delete(id);
    }


    @Data
    static class NoticeRequest {
        String title;
        String explanation;
        Part target;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime deadline;
        List<String> tags;
    }

    @Data
    static class NoticeResponse {
        Long id;
        public NoticeResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class NoticeDto {
        Long id;
        Member member;
        String title;
        String explanation;
        LocalDate date;
        Part target;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime deadline;
        Long like;


        public NoticeDto(Notice notice) {
            this.id = notice.getId();
            this.member = notice.getMember();
            this.title = notice.getTitle();
            this.explanation = notice.getExplanation();
            this.date = notice.getDate();
            this.deadline = notice.getDeadline();
            this.target = notice.getTarget();
            this.like = notice.getLike();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
