package jpabook.jpashop.repository.order.simplequery;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
		
	private final EntityManager em;
	
	// TODO: DTO 바로 조회
		// Order(엔티티)를 바로 조회하는 것에 비해 재사용성 측면에서는 불리하지만 select 절에 필요한 컬럼만 지정할 수 있어서 성능상 최적화가 가능하다 (생각보다 미미하다)
		// 마치 그냥 SQL 짜는 것과 비슷
		// repository 계층은 엔티티그래프를 조회하는 곳인데 이렇게 DTO를 조회하게 되면 계층의 역할에 맞지 않는 모습이 된다
		// 결론: 성능 최적화를 위해 DTO를 조회하는 경우에는 repository 하위에 패키지를 추가해서 그 안에 구현하는 방향을 권장
		public List<OrderSimpleQueryDto> findOrderDtos() {
			return em.createQuery(
					"SELECT new jpabook.jpashop.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
					" FROM Order o" + 
					" JOIN o.member m" + 
					" JOIN o.delivery d", OrderSimpleQueryDto.class
					).getResultList();
		}
	
}
