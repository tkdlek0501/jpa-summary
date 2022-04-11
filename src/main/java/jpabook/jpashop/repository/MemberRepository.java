package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

// TODO: 레포지토리 작업; DB와 연결되는 클래스

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	
//	@PersistenceContext // 스프링이 엔티티매니저를 주입 시켜줌 -> 스프링 jpa에서 @Autowired로 대체 가능
//	private EntityManager em; 
	
	private final EntityManager em;
	
	// 회원 저장
	public void save(Member member) {
		em.persist(member); // 멤버 객체를 넣어줌, transaction 커밋 시점에 insert 쿼리가 DB에 날아감
	}
	
	// 회원 조회
	public Member findOne(Long id) {
		return em.find(Member.class, id); // 단건 조회 메서드 매개변수로 type, PK 넣어주면 됨
	}
	
	// 회원 목록
	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class) // JPQL 쿼리 생성 (엔티티 객체를 대상으로 쿼리) -> SQL로 변환돼서 테이블에서 가져오는 것
			.getResultList(); // list로 결과 가져오기
	}
	
	// 이름으로 회원 조회
	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where m.name = :name", Member.class) // ':파라미터' 로 where 절에 값을 넣을 수 있다.
				.setParameter("name", name) // where 절에 넣을 파라미터 값을 세팅
				.getResultList();
	}
	
}
