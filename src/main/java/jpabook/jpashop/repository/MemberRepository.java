package jpabook.jpashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jpabook.jpashop.domain.Member;

// spring data jpa 적용

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	
	
}
