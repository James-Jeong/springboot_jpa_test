package jpabook.jpashop.api.member.controller;

import jpabook.jpashop.api.common.Result;
import jpabook.jpashop.api.member.dto.*;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
/*@Controller
@ResponseBody*/
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 엔티티 정보가 모두 노출된다.
     * 노출하고 싶은 필드만 노출해야 한다!
     * > 숨기고 싶은 필드에 @JsonIgnore 어노테이션을 달면 되면 해결 된다?
     * > API 스펙마다 케이스가 다양하기 때문에 엔티티 안에 설정하면 안된다!
     * 그래서 엔티티에는 프레젠테이션 레이어 비즈니스 로직이 추가되면 안된다.
     * 엔티티가 변화면 API 스펙이 바뀌게 된다.
     *
     * [
     *     {
     *         "id": 1,
     *         "name": "abc",
     *         "address": null,
     *         "orders": []
     *     }
     * ]
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findAllMembers();
    }

    /**
     * List 형태로 반환하지 않는다.
     * 노출하고 싶은(필요한) 정보만 노출시킨다.
     *
     * {
     *     "data": [
     *         {
     *             "name": "abc"
     *         }
     *     ]
     * }
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> memberList = memberService.findAllMembers();
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * [ API 스펙을 위한 별도의 DTO 가 필요하다. ]
     * API 스펙에 의존된다.
     * 외부에서 엔티티를 맵핑해서 가져오면 안된다.
     * API 요청 스펙이 때에 따라 계속 바뀌게 되므로 엔티티도 바꿔야하게 되는 의존성 문제가 발생하기 때문이다.
     * 엔티티를 외부에 노출하면 안된다. 엔티티를 API 파라미터로 받으면 안된다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     * API 스펙에 의존되지 않는다. (엔티티와 API 스펙을 명확하게 분리)
     * DTO 를 통해 특정 파라미터 필드만 설정해서 API 규격을 확인할 수 있다.
     *
     * CreateMemberRequest 생성자 만들면 안됨
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest createMemberRequest) {
        Member member = new Member(createMemberRequest.getName());
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     *
     * UpdateMemberRequest 생성자 만들면 안됨
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {
        memberService.update(id, updateMemberRequest.getName());
        Member member = memberService.findMember(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }

}
