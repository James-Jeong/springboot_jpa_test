package jpabook.jpashop.common;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.member.Member;

public class MemberCreator {

    public static Member createTestMember(String name) {
        return new Member(name, new Address("Seoul", "guro", "100"));
    }

}
