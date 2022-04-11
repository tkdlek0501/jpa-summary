package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders") // table 이름과 클래스 이름이 다를 경우에는 직접 지정해줘야 한다. 
@Getter @Setter
public class Order {
	
	@Id @GeneratedValue
	@Column(name="order_id") // 실제 테이블 컬럼명과 변수명을 매핑
	private Long id;
	
	
	// 주문 한 건은 한 명의 회원, 한 명은 주문 여러건 가능 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id") // member_id 로 join 되는 것 (FK)
	private Member member;
	
	// orderItems가 다수 (order_item 테이블이 FK 가지고 있다.)
	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	// cascade 설정
	// persist(orderItemA)
	// persist(orderItemB)
	// persist(orderItemC)
	// persist(order)
	// 에서
	// persist(order) 로 사용 가능
	// 즉 order를 persist 할때 orderItems도 persist 됨 (원래는 각각 해야됨)
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	private LocalDateTime orderDate; // 주문시간
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status; // 주문상태 [ORDER, CANCEL]
	
	// TODO: 연관 관계 메서드 사용법
	// == 연관 관계 메서드 == // 양방향 연관 관계
	// ManyToOne
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	// OneToOne
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	// OneToMany
	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	
	
}

// member 와 order 중 연관관계의 주인은 orders 테이블이 FK를 가지기 때문에 order 가 돼야한다. 