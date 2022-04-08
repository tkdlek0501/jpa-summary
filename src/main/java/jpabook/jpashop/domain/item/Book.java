package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // @DiscriminatorColumn에 들어갈 구분명
@Getter 
@Setter
public class Book extends Item{
	
	private String author;
	private String isbn;
	
}
