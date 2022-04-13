package jpabook.jpashop.controller.form;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

// form 클래스는 단순히 화면에서 data를 받아오기 위한 용도로 쓰기위해 만들어 둔 것 
// 엔티티와 구분을 해서 validation 을 더 깔끔하게, 엔티티에 추가적인 코드가 들어가지 않게 할 수 있다.

@Getter @Setter
public class MemberForm {
	
	@NotEmpty(message = "회원 이름은 필수 입니다.")
	private String name;
	
	private String city; 
	private String street;
	private String zipcode;
	
}
