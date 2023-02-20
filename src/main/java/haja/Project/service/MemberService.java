package haja.Project.service;

import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.Member;
import haja.Project.domain.Part;
import haja.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberInfoById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
    public Optional<Member> findByEmail(String email){ return memberRepository.findByEmail(email); }

    @Transactional
    public Long update(Long id, String phone_num, Part part, String comment, String major, String student_id) {
        Member member = findById(id).get();
        member.setPhone_num(phone_num);
        member.setPart(part);
        member.setComment(comment);
        member.setMajor(major);
        member.setStudent_id(student_id);
        memberRepository.save(member);
        return member.getId();
    }
}