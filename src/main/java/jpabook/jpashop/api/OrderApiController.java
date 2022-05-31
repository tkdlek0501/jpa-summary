package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
		
	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;
	
	@GetMapping("/api/v1/orders")
	public List<Order> ordersV1(){
		List<Order> all = orderRepository.findAll(new OrderSearch());
		for(Order order : all) { // 초기화를 위해서 for문 돌리며 get메서드
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream().forEach(o -> o.getItem().getName());
		}
		return all;
	}
	
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> orders = orderRepository.findAll(new OrderSearch());
		List<OrderDto> collect = orders.stream()
			.map(o -> new OrderDto(o))
			.collect(Collectors.toList());
		
		return collect;
	}
	
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		List<Order> orders = orderRepository.findAllWithItem();
		List<OrderDto> collect = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
			
			return collect;
	}
	
	// 컬렉션 조회시 페이징 가능하게 : propertise에서 fetch size를 글로벌 하게 설정 가능
	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3_page(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "offset", defaultValue = "100") int limit
			) {
		List<Order> orders = orderRepository.findAllPaging(offset, limit);
		List<OrderDto> collect = orders.stream()
				.map(o -> new OrderDto(o))
				.collect(Collectors.toList());
			
			return collect;
	}
	
	// 컬렉션 DTO로 바로 조회
	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4(){
		return orderQueryRepository.findOrderQueryDtos();
	}
	
	@Data
	static class OrderDto {
		
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems; // order 뿐만 아니라 모든 엔티티를 DTO로 변환해야 된다
		
		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			orderItems = order.getOrderItems().stream()
					.map(orderItem -> new OrderItemDto(orderItem))
					.collect(Collectors.toList());
		}
		
	}
	
	@Getter
	static class OrderItemDto {
		
		// API 필요 스팩을 아래와 같이 정한다고 가정 (depth를 줄이기 위해)
		private String itemName; // 상품명
		private int orderPrice; // 주문 가격
		private int count; // 주문 수량
		
		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
	
}
