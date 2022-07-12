package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@Transactional(readOnly = true) // 읽기만 할 때는 readOnly 를 적용하여 성능 최적화
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ReentrantLock memberJoinLock = new ReentrantLock();

    @Transactional
    public Long join(Member member) {
        memberJoinLock.lock();
        try {
            validateDuplicateMember(member);
            return memberRepository.save(member);
        } catch (Exception e) {
            log.warn("", e);
            throw e;
        } finally {
            memberJoinLock.unlock();
        }
    }

    private void validateDuplicateMember(Member member) {
        if (memberRepository.countByName(member.getName()) > 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Member findMember(Long memberId) {
        return memberRepository.find(memberId);
    }

    @Transactional
    public void update(Long memberId, String memberName) {
        Member member = memberRepository.find(memberId);
        if (member != null) {
            member.change(memberName, null);
        }
    }

}
