package jpabook.jpashop.domain.config;

import jpabook.jpashop.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class MemberConfig {

    @Autowired
    public MemberRepository getMemberRepository(EntityManager entityManager) {
        return new MemberRepository(entityManager);
    }

}
