package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
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
	
	// 등록
	@PostMapping("/v2/members")
	public CreateMemberResponse saveMemberV2(
			@RequestBody @Valid CreateMemberRequest request
			){
		
		Member member = new Member();
		member.setName(request.getName());
		
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	// 수정
	@PutMapping("/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(
			@PathVariable("id") Long id,
			@RequestBody @Valid UpdateMemberRequest request
			) {
		
		memberService.update(id, request.getName()); // update 만 하고 끝내도록 로직 구현
		Member findMember = memberService.findMember(id); // member 다시 조회 ; 위 update 로직에서 바로 조회할 수는 있으나 구분하는게 더 낫다
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}
	
	// 조회 1
	@GetMapping("/v1/members")
	public List<Member> membersV1(){
		return memberService.findMembers();
	}
	
	// 조회 2 ; Entity 반환 대신 DTO로 반환  
	@GetMapping("/v2/members")
	public Result memberV2() {
		List<Member> findMembers = memberService.findMembers();
		List<MemberDto> collect = findMembers.stream()
		.map(m -> new MemberDto(m.getName()))
		.collect(Collectors.toList());
		
		return new Result(collect.size(), collect);
	}
	
	@Data
	@AllArgsConstructor
	static class Result<T>{
		private int count;
		private T data; // data를 한번 묶어야 json 형식이 array가 아니라 {}로 먼저 묶이게 된다
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto{
		private String name;
	}
	
	@Data
	static class UpdateMemberRequest{
		private String name;
	}
	
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse{
		private Long id;
		private String name;
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
