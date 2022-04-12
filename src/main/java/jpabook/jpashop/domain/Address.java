package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;

// jpa 내장타입
// 값 타입은 변경 불가능하게 설계해야한다.
// 따라서 set 제공하지 않고 생성자로만 쓸 수 있게 + 기본 생성자를 protected로 만들어서 접근 못하게 

@Embeddable
@Getter
public class Address {
	
	private String city;
	private String street;
	private String zipcode;
	
	protected Address() {
	}
	
	public Address(String city, String street, String zipcode) {
		 this.city = city;
		 this.street = street;
		 this.zipcode = zipcode;
	}
	
}
