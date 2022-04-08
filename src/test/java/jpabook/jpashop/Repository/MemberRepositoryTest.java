package jpabook.jpashop.Repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.Entity.Member;

@RunWith(SpringRunner.class) // junit에게 스프링 관련 테스트를 할거다라는 것을 알려주기
@SpringBootTest // 스프링 테스트 사용
public class MemberRepositoryTest {
	
	@Autowired MemberRepository memberRepository;;
	
	@Test
	@Transactional // em을 통한 data 변경은 항상 transaction 안에서 이뤄져야한다!!! -> @Test 안에 있으면 실행 후 롤백됨
	@Rollback(false) // 롤백되지 않게 설정
	public void testMember() throws Exception {
		//given
		Member member = new Member();
		member.setUsername("memberA");
		
		//when
		Long savedId = memberRepository.save(member);
		Member findMember = memberRepository.find(savedId);
		
		//then
		// id로 찾은 멤버의 id 가 내가 저장할 때 반환 받은 id와 같은지
		Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
		// findMember == member
		Assertions.assertThat(findMember).isEqualTo(member); // transaction 안에서는 (같은 영속성 컨텍스트 안에서는) 식별자가 같으면 같은 엔티티로 판단
	}
	
}
