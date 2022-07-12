package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.controller.form.MemberForm;
import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipCode());
        Member member = new Member(memberForm.getName(), address);
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // 가능하면 엔티티말고 DTO 를 따로 만들어서 출력하는 것을 권장
        List<Member> allMembers = memberService.findAllMembers();
        model.addAttribute("members", allMembers);
        return "members/memberList";
    }

}
