package jpabook.jpashop.service;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class) // junit 실행시 스프링과 같이 실행하기 위해서
@SpringBootTest // 스프링부트를 띄운 상태로 테스트 실행하기 위해서(@Autowired 등)
@Transactional
@Slf4j
public class MemberServiceTest {
	
	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;
	
	// 회원 가입
	@Test
	// @Rollback(false)  // 롤백을 하지 않게 설정해서 실제 DB에 들어가는 것 확인 가능
	public void join() throws Exception {
		// given
		Member member = new Member();
		member.setName("kim");
		
		// when
		Long savedId = memberService.join(member);
		
		// then
		em.flush(); // insert 쿼리를 보기 위해서 추가(쿼리 동작을 강제로); 원래는 @Transactional에 의해 test는 롤백됨 애초에 insert 쿼리가 DB로 나가지 않음
		Assertions.assertEquals(member, memberRepository.findById(savedId));
	}
	
	
	// 중복 회원 예외
//	@Test(expected = IllegalStateException.class)
//	public void validation() throws Exception {
//		// given
//		Member member1 = new Member();
//		member1.setName("HJ");
//		
//		Member member2 = new Member();
//		member2.setName("kim");
//		
//		// when
//		memberService.join(member1); // kim 이란 이름으로 회원 가입
//		memberService.join(member2); // 같은 이름으로 회원 가입
//		
//		// then
//		Assertions.fail("예외가 발생해야 한다."); // 테스트 상 이 코드를 타면 안 된다.
//	}
}
