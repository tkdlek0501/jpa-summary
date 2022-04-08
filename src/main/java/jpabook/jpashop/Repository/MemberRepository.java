package jpabook.jpashop.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.Entity.Member;

// Repository: 엔티티를 찾아주는 역할, DAO와 비슷하다고 생각

@Repository
public class MemberRepository {
	
	@PersistenceContext // 엔티티 관리자 빈 등록 -> 엔티티 관리자를 통해 data 관리를 하게 되고 data 변경은 항상 transaction 안에서 이뤄져야 함
	private EntityManager em;
	
	// 회원 저장 메서드 (회원 저장 후 id 반환)
	public Long save(Member member) {
		em.persist(member);
		return member.getId(); // 저장한 멤버를 그대로 가져오는 것은 안좋다. 커맨드와 커리의 분리?
	}
	
	// 특정 멤버 조회 (id 로 멤버 가져오기)
	public Member find(Long id) {
		return em.find(Member.class, id);
	}
	
	
}
