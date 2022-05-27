package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	
	private final EntityManager em;
	
	public void save(Order order) {
		em.persist(order);
	}
	
	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}
	
	// 검색 포함 전체 조회 - String으로 jpql 만들기; 잘못된 부분을 찾기가 너무 힘들다...
	public List<Order> findAll(OrderSearch orderSearch){
		
		String jpql = "select o from Order o join o.member m";
		boolean isFirstCondition = true;
		
		// 주문 상태 검색 
		if (orderSearch.getOrderStatus() != null) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		
		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if(isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and"; 
			}
			jpql += " m.name like :name";
		}
		
		TypedQuery<Order> query = em.createQuery(jpql, Order.class)
									.setFirstResult(0) // offset
									.setMaxResults(1000); // limit	
				
		if(orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
				
		return query.getResultList();
	}
	
	// TODO: fetch join 사용
	// fetch join은 객체를 기준으로 join을 해오는 것, 즉 order 테이블을 조회하는 개념이 아니라 Order 객체를 조회하는 개념
	// inner join이 기본
	public List<Order> findAllWithMemberDelivery() {
		return em.createQuery(
					"SELECT o FROM Order o" +
					" LEFT JOIN FETCH o.member m" +
					" LEFT JOIN FETCH o.delivery d", Order.class
				).getResultList();
	}
	
	// TODO: 동적 쿼리 예시 (Querydsl 라이브러리 이용)
	// 검색 포함 전체 조회 - Querydsl 로 처리
//	public List<Order> findAll(OrderSearch orderSearch){
//		QOrder order = QOrder.order;
//		QMember member = QMember.member;
//		
//		return query
//				.select(order)
//				.from(order)
//				.join(order.member, member)
//				.where(statusEq(orderSearch.getOrderStatus()),
//						nameLike(orderSearch.getMemberName()))
//				.limit(1000)
//				.fetch();
//	}	
//	
//	private BooleanExpression statusEq(OrderStatus statusCond) {
//		if(statusCond == null) {
//			return null;
//		}
//		return order.status.eq(statusCond);
//	}
//	
//	private BooleanExpression nameLike(String nameCond) {
//		if(!StringUtils.hasText(nameCond)) {
//			return null;
//		}
//		return member.name.like(nameCond);
//	}
}
