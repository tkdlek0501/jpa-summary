package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.controller.form.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {
	
	private final ItemService itemService;
	
	// 상품 등록 폼
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "item/createItemForm";
	}
	
	// 상품 등록
	@PostMapping("/items/new")
	public String create(@Validated BookForm form, BindingResult result) {
		
		log.info("bookForm : {}", form);
		
//		Book book = new Book();
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());
		// 이렇게 BookForm 객체를 생성해서 외부에서 set하는 것은 안좋다
		// BookForm 객체 내에서 생성 메서드를 만든 뒤 호출하는 게 나음
		Book savedBook = Book.createBook(null, form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
		// order 컨트롤러에서는 등록하는 메서드 내에서 영속성 컨텍스트를 만들어서 거기서 객체의 생성 메서드(static)를 호출
		itemService.saveItem(savedBook);
		
		return "redirect:/items";
	}
	
	// 상품 목록
	@GetMapping("/items")
	public String itemList(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "item/itemList";
	}
	
	// 상품 수정 폼
	@GetMapping("items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book item = (Book) itemService.findOne(itemId); // 예제에서는 Book만 쓴다고 가정했기 때문에 굳이 캐스팅 한 것
		
		BookForm form = new BookForm();
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());
		
		
		model.addAttribute("form", form);
		return "item/updateItemForm";
	}
	
	// TODO: JPA에서의 수정은 병합이 아닌 변경감지를 지향 
	// 상품 수정
	@PostMapping("items/{itemId}/edit")
	public String updateItem(@ModelAttribute("form") BookForm form) {
		
		//Book updatedBook = book.createBook(form.getId(), form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
		// DB를 거쳐서 식별자인 id를 가지고 있기 때문에 Book은 준영속 엔티티이다 
		
		itemService.updateItem(form.getId(), form.getPrice(), form.getName(), form.getStockQuantity()); // 여기서 transactinal에 의한 변경 감지로 update
		
		return "redirect:/items";
	}
	
}
