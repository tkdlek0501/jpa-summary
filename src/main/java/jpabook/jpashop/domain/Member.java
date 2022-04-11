package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

// 실무에서는 Getter는 열어놓되, Setter는 열어놓으면 어디서 변경됐는지 알기 어려워 비즈니스 메서드로 대신하는 게 좋다

@Entity
@Getter @Setter
public class Member {
	
	@Id @GeneratedValue
	private Long id;
	
	private String name;
	
	// 회원은 주소 정보를 포함
	@Embedded // jpa 내장타입 사용
	private Address address;
	
	
	// 관계 매핑
	// 회원 한명은 주문과 1대 다 관계
	@OneToMany(mappedBy="member") // 매핑을 당하는 것 (연관관계 주인이 아니면 이렇게 해줘야한다.)
	private List<Order> orders = new ArrayList<>();
	
}
