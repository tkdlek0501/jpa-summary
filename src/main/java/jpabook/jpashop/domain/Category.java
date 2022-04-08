package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
	
	@Id @GeneratedValue
	@Column(name = "category_id")
	private Long id;
	
	private String name; 
	
	@ManyToMany 
	@JoinTable(name="category_item", // join을 위한 매핑용 테이블
			joinColumns = @JoinColumn(name = "category_id"),
			inverseJoinColumns = @JoinColumn(name = "item_id")) 
	private List<Item> items = new ArrayList<>();
	
	
	// TODO: self로 양방향 연관 관계
	// 해당 카테고리의 부모 id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;
	
	// 해당 카테고리의 자식들
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();
}
