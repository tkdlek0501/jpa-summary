package jpabook.jpashop.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity // JPA가 관리하는 클래스, 테이블과 매핑할 클래스로 지정
@Getter @Setter
public class Member {
	
	@Id @GeneratedValue // Id: PK로 지정, GeneratedValue: 키 생성 자동할당 // 이 두개는 기본키를 자동으로 생성할 때 필요
	private Long id;
	private String username;
	
}
