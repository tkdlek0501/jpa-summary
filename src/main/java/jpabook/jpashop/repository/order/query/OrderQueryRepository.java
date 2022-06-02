package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
	
	private final EntityManager em;
	
	public List<OrderQueryDto> findOrderQueryDtos() {
		List<OrderQueryDto> result = findOrders();
		
		// 여기서 orderItem을 set 해줌; for문으로 쿼리를 돌리게 돼서 지양해야 하는 로직 (이것도 결국 N+1)
		result.forEach(o -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
			o.setOrderItems(orderItems); 
		});
		
		 return result;
	}
	
	// 위 for문 쓰던 방법에서 in 쿼리로 변경해서 orderItem 조회 쿼리는 한번에 가져오도록 변경
	// TODO: *1:N 컬렉션 DTO로 바로 조회 최적 방법
	public List<OrderQueryDto> findAllByDto_optimization() {
		// 1. order 조회
		List<OrderQueryDto> result = findOrders(); 
		
		// 2. 모든 orderId List
		List<Long> orderIds = result.stream()
		.map(o -> o.getOrderId())
		.collect(Collectors.toList());
		
		// 3. orderId로 orderItem 조회
		List<OrderItemQueryDto> orderItems = em.createQuery(
			"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
					" from OrderItem oi" +
					" join oi.item i" +
					" where oi.order.id in :orderIds", OrderItemQueryDto.class)
			.setParameter("orderIds", orderIds)
			.getResultList();
		
		// 4. groupingBy를 이용해서 key:orderId로 value:orderItems(List)를 묶어서 Map으로 변환
		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
			.collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
		
		// 5. forEach 돌리며 넣어주기 (애플리케이션에서 for문)
		result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId()))); // orderItem 넣어줌
		
		return result;
	}
	
	// 위 방법에서 sql 한번에 가져오도록 변경
	// 문제점: N쪽을 기준으로 row가 생성됨 (중복 있음), 한번에 가져오기 때문에 API 스펙에 어긋남
	public List<OrderFlatDto> findAllByDto_flat() {
		return em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +		
				" from Order o" +
				" join o.member m" +
				" join o.delivery d" + 
				" join o.orderItems oi" +
				" join oi.item i", OrderFlatDto.class)
				.getResultList();
	}
	
	private List<OrderItemQueryDto> findOrderItems(Long orderId){
		return em.createQuery(
				"select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
				" from OrderItem oi" +
				" join oi.item i" +
				" where oi.order.id = :orderId", OrderItemQueryDto.class
				).setParameter("orderId", orderId)
				.getResultList();
	}
	
	private List<OrderQueryDto> findOrders(){
		return em.createQuery(
				"SELECT new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) FROM Order o" +
				" JOIN o.member m" + 
				" JOIN o.delivery d", OrderQueryDto.class
				).getResultList();
	}

	

	
 }








