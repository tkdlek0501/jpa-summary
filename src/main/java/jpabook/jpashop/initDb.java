package jpabook.jpashop;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class initDb {
	
	private final InitService initService;
	
	@PostConstruct 
	public void init() {
		initService.dbInit1();
		initService.dbInit2();
	}
	
	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		private final EntityManager em;
		public void dbInit1() {
			// 회원 등록
			Member member = createMember("userA", "서울", "1", "11111");
			em.persist(member);
			
			// 상품 등록
			Book book1 = new Book();
			book1.setName("jpa book1");
			book1.setPrice(10000);
			book1.setStockQuantity(100);
			em.persist(book1);
			
			Book book2 = new Book();
			book2.setName("jpa book2");
			book2.setPrice(20000);
			book2.setStockQuantity(100);
			em.persist(book2);
			
			// 주문
			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
			
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}
		
		public void dbInit2() {
			// 회원 등록
			Member member = createMember("userB", "경기", "2", "22222");
			em.persist(member);
			
			// 상품 등록
			Book book1 = new Book();
			book1.setName("jpa book3");
			book1.setPrice(30000);
			book1.setStockQuantity(200);
			em.persist(book1);
			
			Book book2 = new Book();
			book2.setName("jpa book4");
			book2.setPrice(40000);
			book2.setStockQuantity(200);
			em.persist(book2);
			
			// 주문
			OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
			
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
			em.persist(order);
		}

		private Member createMember(String name, String city, String distance, String zipcode) {
			Member member = new Member();
			member.setName(name);
			member.setAddress(new Address(city, distance, zipcode));
			return member;
		}
	}
	
}
