package jpabook.jpashop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
	
	private final EntityManager em;
	
	// save OR update
	public void save(Item item) {
		if(item.getId() == null) {
			em.persist(item); // 새로 생성
		}else {
			em.merge(item); // 갱신 but merge는 보통 쓰지 않는다 (대신 변경 감지를 이용)
			// item은 영속성 컨텍스트로 바뀌지 않음, Item 객체 인스턴스에 대입 후 사용해야 됨
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll(){
		return em.createQuery("select i from Item i", Item.class)
				.getResultList();
	}
}
