package jpabook.jpashop.repository.order.query;

import java.time.LocalDateTime;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

// order와 orderitem을 join해서 한줄로 가져오기 위한 DTO

@Data
public class OrderFlatDto {
	
	// order
	private Long orderId;
	
	private String name;
	
	private LocalDateTime orderDate;
	
	private OrderStatus orderStatus;
	
	private Address address;
	
	// orderitem
	private String itemName;
	
	private int orderPrice;
	
	private int count;

	public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address,
			String itemName, int orderPrice, int count) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}
	
	
}
