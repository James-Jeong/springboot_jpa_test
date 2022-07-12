package jpabook.jpashop.service;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.fail;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testJoinMember() throws Exception {
        // 1) Given
        Member member = new Member("test_member", new Address("Seoul", "guro", "100"));

        // 2) When
        Long joinedMemberId = memberService.join(member);

        // 3) Then
        Member joinedMember = memberService.findMember(joinedMemberId);
        log.info("joinedMember: {}", joinedMember);

        Assertions.assertEquals(member, joinedMember);
    }

    @Test(expected = IllegalStateException.class)
    public void testDuplicatedMember() throws Exception {
        // 1) Given
        Member memberA = new Member("memberA", new Address("Seoul", "guro", "100"));
        Member memberB = new Member("memberA", new Address("Seoul", "dongjak", "102"));

        // 2) When
        memberService.join(memberA);
        /*assertThatThrownBy(
                () -> memberService.join(memberB)
        ).isInstanceOf(IllegalStateException.class);*/
        memberService.join(memberB);

        // 3) Then
        fail("IllegalStateException.class");
    }

}
