package jpabook.jpashop.repository;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    //@Rollback(value = false)
    public void testSaveAndFindMember() throws Exception {
        // 1) Given
        Member member = new Member("memberA", new Address("Seoul", "Dongjak", "82"));

        // 2) When
        Long memberId = memberRepository.save(member);
        Member memberA = memberRepository.find(memberId);

        // 3) Then
        Assertions.assertThat(memberA.getId()).isEqualTo(member.getId());
        Assertions.assertThat(memberA.getName()).isEqualTo(member.getName());
        Assertions.assertThat(memberA).isEqualTo(member); // 영속성 컨텍스트의 1차 캐시
    }

}