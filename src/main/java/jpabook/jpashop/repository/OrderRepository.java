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
	
	// 1:N 컬렉션을 join 해서 가져오면 데이터 수가 뻥튀기 됨 (1쪽의 데이터는 중복이어도 N쪽의 데이터가 다르니까)
	// -> jpql의 distinct 는 식별자가 같은 data의 중복을 제거해준다
	// 주의점1: 1:다 관계를 fetch join 하면 paging 불가능 (쿼리가 아닌 메모리 단계에서 페이징 처리를 하게됨)
	// 주의점2: 1:다 관계 fetch join은 1개만 사용해야 한다, 1:N:N:... 이렇게 하면 데이터가 부정합하게 조회될 수 있다 	
	public List<Order> findAllWithItem() {
		return em.createQuery(
				"SELECT DISTINCT o FROM Order o" +
				" JOIN FETCH o.member m" +
				" JOIN FETCH o.delivery d" +		
				" JOIN FETCH o.orderItems oi" +
				" JOIN FETCH oi.item i", Order.class)
				.setFirstResult(1)
				.setMaxResults(100)
				.getResultList();
	}
	// + fetch join 하므로 엔티티를 기준으로 끌고오는데, fetch join에 걸려있지 않으면 fetch type을 따라 뒤에 쿼리가 날아감(N + 1)
	// + 즉시로딩은 지연로딩과 다르게 전체를 join해서 한방에 가져오지만 어떤 join인지를 예상하기 힘들고 모든 연관된 엔티티를 join하기 때문에 사용을 지양하는 것
	
	// 페이징 사용
	public List<Order> findAllPaging(int offset, int limit) {
		return em.createQuery(
				"SELECT o FROM Order o" +
				" LEFT JOIN FETCH o.member m" +
				" LEFT JOIN FETCH o.delivery d", 
				Order.class
				).setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
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
