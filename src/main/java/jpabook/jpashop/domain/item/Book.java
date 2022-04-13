package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // @DiscriminatorColumn에 들어갈 구분명
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item{
	
	private String author;
	private String isbn;
	
	// === 생성 메서드
	public static Book createBook(Long id, String name, int price, int stockQuantity, String author, String isbn) {
		
		Book book = new Book();
		
		book.setId(id);
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		book.setAuthor(author);
		book.setIsbn(isbn);
		
		return book;
	}
	
}
