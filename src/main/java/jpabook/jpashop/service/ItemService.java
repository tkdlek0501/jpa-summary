package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) // 한번에 수행되어야 할 연산 처리
@RequiredArgsConstructor
public class ItemService { // 이 클래스도 물론 테스트 코드 필요
	
	private final ItemRepository itemRepository;
	
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	
	public List<Item> findItems() {
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
	
	// TODO: 상품 수정 - ※변경 감지 이용 (준영속 엔티티 수정)
	/* 영속성 컨텍스트가 자동 변경 */
	@Transactional
	public Item updateItem(Long itemId, int price, String name, int stockQuantity) {
		Item findItem = itemRepository.findOne(itemId);
		
		findItem.setPrice(price);
		findItem.setName(name);
		findItem.setStockQuantity(stockQuantity);
		// findItem.change(price, name, stockQuantity); // 위에 처럼 set으로 변경하기 보다는 이것처럼 의미있는 메서드로 만들어주기
		
		return findItem;
	}
	// transactional 에 의해 transaction이 commit 되고 JPA는 플러시 됨
	// 영속성 컨텍스트인 엔티티 중 변경된 것을 찾아내서 자동으로 update 해줌
}
