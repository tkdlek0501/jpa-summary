package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
		
	private final MemberService memberService;
	
	// 회원 생성 폼
	@GetMapping("/members/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberForm()); // 비어있는 객체지만 이를 통해 view 에서 validation 할 수 있으므로 포함
		return "member/createMemberForm";
	}
	
	// 회원 등록
	@PostMapping("/members/new")
	public String create(@Validated MemberForm form, BindingResult result) { // BindingResult를 넣으면 validation에 의해 error 가 발생해도 컨트롤러가 실행됨 (에러를 대신 갖고 있음)
		
		if(result.hasErrors()) {
			return "member/createMemberForm";
		}
		
		// MemberForm 으로 들어온 data를 엔티티에 맞게 가공 
		Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
		
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		
		memberService.join(member);
		return "redirect:/";
	}
	
	// 회원 목록
	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers();
		model.addAttribute("members", members);
		return "member/memberList";
	}
	
	// TODO: 엔티티 사용시 주의점
	/*
	 * 템플릿을 이용해 view 단으로 엔티티를 넘기는 것은 사용해도 괜찮음 (타임리프로 선택적으로 data 공개)
	 * 그러나 API 응답으로 엔티티를 넘겨버리면 안된다. API 스펙에 공개하면 안되는 data까지 포함되니까
	 * 결론 : 템플릿도 API도 외부로 data를 넘길 때는 화면용 DTO에 담아서 보내주는 것을 지향해야 한다.
	 * */
}
