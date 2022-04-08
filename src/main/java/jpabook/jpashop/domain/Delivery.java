package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
	
	@Id @GeneratedValue
	@Column(name="delivery_id")
	private Long id;
	
	@OneToOne(mappedBy = "delivery")
	private Order order;
	
	@Embedded
	private Address address; // 하나의 객체로 만든 공통 부분?
	
	@Enumerated(EnumType.STRING) // ※enum을 넣을 때 ordinary 대신 String 쓰기! (ord- 는 숫자로 값이 들어가서 enum을 추후에 변경해버리면 답이 없다.)
	private DeliveryStatus status; // READY, COMP
}
