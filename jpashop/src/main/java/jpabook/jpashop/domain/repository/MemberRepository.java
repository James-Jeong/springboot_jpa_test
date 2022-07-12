package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager entityManager; // 자동 주입 (EntityManagerFactory)

    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        return entityManager.createQuery(
                        "select m from Member m",
                        Member.class
                )
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return entityManager.createQuery(
                "select m from Member m where m.name = :name",
                        Member.class
                )
                .setParameter("name", name)
                .getResultList();
    }

    public long countByName(String name) {
        return entityManager.createQuery(
                "select count(m) from Member m where m.name = :name",
                Long.class
                )
                .setParameter("name", name)
                .getSingleResult();
    }

}
