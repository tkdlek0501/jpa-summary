package jpabook.jpashop.controller.form;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class BookForm {
	
	// 수정시 필요 (등록폼, 수정폼 나누는게 낫다)
	private Long id;
	
	// item 공통
	private String name;
	private int price;
	private int stockQuantity;
	
	// book 
	private String author;
	private String isbn;
	
}
