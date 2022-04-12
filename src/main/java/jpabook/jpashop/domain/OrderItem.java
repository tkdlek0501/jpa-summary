package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서는 set을 사용하지 않고 메서드를 통해서만 생성이 되게
public class OrderItem {
	
	@Id @GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY) // N : 1
	@JoinColumn(name = "item_id")
	private Item item;
	
	
	@ManyToOne(fetch = FetchType.LAZY) // N : 1 관계
	@JoinColumn(name = "order_id") // N 쪽에 FK 가 있으므로 연관관계의 주인
	private Order order;
	
	private int orderPrice; // 주문 가격
	private int count; // 주문 수량
	
	// == 생성 메서드 == //
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) { // order는 왜 안받지???
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		
		item.removeStock(count);
		return orderItem;
	}
	
	// == 비즈니스 로직 == //
	public void cancel() {
		getItem().addStock(count); // 주문수량 원복
	}
	
	// == 조회 로직 == //
	// 개별 주문 가격 조회
	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}
}
