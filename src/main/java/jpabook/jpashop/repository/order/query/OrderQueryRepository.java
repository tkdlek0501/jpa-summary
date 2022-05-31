package jpabook.jpashop.repository.order.query;

import java.util.List;

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
