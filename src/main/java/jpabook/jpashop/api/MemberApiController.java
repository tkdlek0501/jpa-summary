package jpabook.jpashop.api;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {
	
	private final MemberService memberService;
	
	@PostMapping("/v1/members")
	public CreateMemberResponse saveMemberV1(
			@RequestBody @Valid Member member // 이렇게 Entity로 받아오는 것은 제약이 많아서 DTO를 만들어서 받는게 훨씬 좋다
			) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	// @RequestBody : json 으로 온 body 내 data들을 자동으로 매핑
	
	@PostMapping("/v2/members")
	public CreateMemberResponse saveMemberV2(
			@RequestBody @Valid CreateMemberRequest request
			){
		
		Member member = new Member();
		member.setName(request.getName());
		
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@Data
	static class CreateMemberRequest{
		private String name;
	}
	
	@Data
	static class CreateMemberResponse{
		private Long id;
		
		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}
	
}
