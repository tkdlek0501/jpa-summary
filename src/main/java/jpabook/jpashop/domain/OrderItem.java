package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {
	
	@Id @GeneratedValue
	@Column(name = "order_item_id")
	private Long id;
	
	@ManyToOne // N : 1
	@JoinColumn(name = "item_id")
	private Item item;
	
	
	@ManyToOne // N : 1 관계
	@JoinColumn(name = "order_id") // N 쪽에 FK 가 있으므로 연관관계의 주인
	private Order order;
	
	private int orderPrice; // 주문 가격
	private int count; // 주문 수량
}
