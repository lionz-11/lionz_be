package haja.Project.service;

import haja.Project.api.dto.MemberRequestDto;
import haja.Project.api.dto.MemberResponseDto;
import haja.Project.domain.Image;
import haja.Project.domain.Member;
import haja.Project.domain.Part;
import haja.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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
    public void deleteImage(Member member) {
        member.setImage(null);
        memberRepository.save(member);
    }

    @Transactional
    public Long updateComment(Long id, String comment) {
        Member member = findById(id).get();
        member.setComment(comment);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Long updatePassword(Long id, String password) {
        Member member = findById(id).get();
        member.setPassword(password);
        memberRepository.save(member);
        return member.getId();
    }
    @Transactional
    public void setImage(Member member, Image image) {
        member.setImage(image);
        memberRepository.save(member);
    }

    @Transactional
    public void setTokenCount(Member member, LocalDateTime date) {
        member.setAccessTokenExpiresIn(date);
        memberRepository.save(member);
    }

    @Transactional
    public void updateCount(Member member,Integer c){
        member.setCount(c + 1);
        memberRepository.save(member);
    }
}