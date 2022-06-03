package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	
	private final MemberRepositoryOld memberRepository;
	private final ItemRepository itemRepository;
	private final OrderRepository orderRepository;
	
	/* 
	 * 주문 (생성)
	*/
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		
		// 엔티티 조회
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		// 배송정보 생성
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		
		// 주문상품 생성 (여러건 넘길 수도 있지만 예제에서는 하나로 -> 여러건 구입은 결국엔 장바구니 기능이 필요함)
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		//OrderItem orderItemTest = new Orderitem(); setter 사용 protected로 막음
		
		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);
		
		// 주문 저장 cascade 때문에 order를 persist 할때 orderItem, delivery도 persist 된다.
		// TODO: cascade 를 남용하면 안되고 order 가 orderItem과 delivery를 관리하므로 걸어준 것 (order를 제외한 다른 곳에서 참조하고 있지 않음)
		// 처음에는 cascade의 기준을 세우기 어려우므로 안쓰고 repository를 따로 관리 하는게 나을 수도 있다. 리팩토링할 때 수정해도 된다.
		orderRepository.save(order);
		
		return order.getId();
	} 
	
	/* 
	 * 주문 취소
	*/ 
	@Transactional
	public void cancelOrder(Long orderId) {
		// 주문 엔티티 조회
		Order order = orderRepository.findOne(orderId);
		// 주문 취소
		order.cancel(); // 비즈니스 로직이 order 클래스 내에 있다. -> *도메인 모델 패턴
	}
	
	// 검색
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAll(orderSearch);
	}
}
