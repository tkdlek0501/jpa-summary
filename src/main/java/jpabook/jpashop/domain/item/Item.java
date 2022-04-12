package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 테이블 전략: single_table; 하나의 테이블에 모든 컬럼 관리
@DiscriminatorColumn(name="dtype") // 위 전략을 사용하기 위한 컬럼 지정
@Getter @Setter
public abstract class Item { // 추상 클래스 ; 타입에 따라 받아올 data가 다르기 때문 (상품의 종류가 여러개라서: book, album, movie)
		
	@Id @GeneratedValue
	@Column(name = "item_id")
	private Long id;
	
	// 공통
	private String name;
	private int price;
	private int stockQuantity;
	 
	@ManyToMany(mappedBy = "items") // 사실상 ManyToMany 는 사용하면 안된다 (중간 테이블이 매핑의 역할로만 쓰이는게 아니라 컬럼이 필요할 때가 생기기 때문, 따라서 중간 테이블을 엔티티로 승격시켜줘야 한다.)
	private List<Category> categories = new ArrayList<Category>();
	
	
	// TODO: 객체 내의 필드와 관련된 비즈니스 로직은 객체 내에 메서드를 만드는 것이 좋은 방법 (getter, setter 의존도를 낮추고 응집도 높고 객체지향적인 설계) 
	// getter 나 setter 사용은 최대한 안해야 좋다... 필요하다면 그 객체 내에 메서드를 만들어서 사용
	//==비즈니스 로직==//
	
	/*
	 재고 증가
	 */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	/* 
	 * 재고 감소 
	*/
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if(restStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restStock;
	}
}
