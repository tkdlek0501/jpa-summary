package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // jpa를 이용할 때 data 변경 로직을 처리할 때는 반드시 transaction 안에서 이뤄져야 한다.(지연로딩 등이 가능)
// 보통 조회 메서드가 많으므로 기본적으로 readOnly 설정을 해줘서 읽기전용이 되도록 해주는게 좋다.
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	// 회원 가입; 저장 후 id 반환
	@Transactional // class 레벨에서 설정한 transactional 과 다르게 설정하고 싶으면 메서드 레벨에서 설정하면 된다.
	public Long join(Member member) {
		
		validateDuplicateMember(member);
		
		memberRepository.save(member);
		return member.getId();
	}
	
	// 중복 회원 검증 (같은 이름이 있는지)
	@Transactional
	private void validateDuplicateMember(Member member) {
		//EXCEPTION
		List<Member> findMembers = memberRepository.findByName(member.getName()); // 이것보다는 count를 해서 0이 아니면 예외 발생 시키는게 좋다
		// 멀티 쓰레드 환경이기 때문에 만약 여러명이 동시에 접근한다면 제대로 실행되지 않을 가능성이 생긴다 -> 최후의 방법은 DB에서 member의 name을 유니크하게 설정 해두는 것 
		if(!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}
	
	// 회원 목록 조회
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}
	
	// 회원 단일 조회
	public Member findMember(Long id) {
		return memberRepository.findOne(id);
	}
	
}
