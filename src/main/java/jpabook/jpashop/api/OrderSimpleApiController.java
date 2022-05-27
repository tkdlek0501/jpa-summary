package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// Order
// Order -> Member (N:1)
// Order -> Delivery (1:1)  
// x To One

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderSimpleApiController {
	
	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;
	
	@GetMapping("/v1/simple-orders")
	public List<Order> orderV1() {
		List<Order> all = orderRepository.findAll(new OrderSearch());
		return all;
	}
	// json 객체로 반환시에 순환참조(ex. order <-> member) 에러 발생; @JsonIgnore
	// but 지연로딩 쓰면 get해서 초기화 하기 전에는 proxy 객체인데  json에서 파싱하려고 하니까 에러 발생

	@GetMapping("v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAll(new OrderSearch());
		
		// TODO: Entity -> DTO 변환 방법 (stream 사용)
		List<SimpleOrderDto> result = orders.stream()
				.map(o -> new SimpleOrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}
	
	// fetch join으로 조회한 Entity를 DTO로 변환
	@GetMapping("v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithMemberDelivery();
		
		List<SimpleOrderDto> result = orders.stream()
				.map(o -> new SimpleOrderDto(o))
				.collect(Collectors.toList());
		
		return result;
	}
	
	// jpql에서 DTO 바로 조회 
	@GetMapping("v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		List<OrderSimpleQueryDto> orders = orderSimpleQueryRepository.findOrderDtos();
		return orders;
	}
	
	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
		}
	}
}
