package com.bookstore.bookstoreapp.resource;

import com.bookstore.bookstoreapp.domain.Book;
import com.bookstore.bookstoreapp.service.BookService;
import com.bookstore.bookstoreapp.service.impl.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookResource {
	
	@Autowired
	private BookService bookService;

	@Autowired
	private S3Service s3Service;
	
	@RequestMapping(value="/add", method= RequestMethod.POST)
	public Book addBookPost(@RequestBody Book book) {
		return bookService.save(book);
	}
	
	@RequestMapping(value="/add/image", method= RequestMethod.POST)
	public ResponseEntity upload(@RequestParam("id") Long id, HttpServletRequest request ){
		try {
			Book book = bookService.findOne(id);

			if (book != null) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Iterator<String> it = multipartRequest.getFileNames();
				MultipartFile multipartFile = multipartRequest.getFile(it.next());
				String bookImageUrl = s3Service.storeBookImage(multipartFile, book.getId().toString());
				book.setBookImageUrl(bookImageUrl);
				bookService.save(book);
				return new ResponseEntity("Upload Success!", HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Upload failed!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/update/image", method= RequestMethod.POST)
	public ResponseEntity updateImagePost( @RequestParam("id") Long id, HttpServletRequest request){
		try {
			Book book = bookService.findOne(id);

			if (book != null) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Iterator<String> it = multipartRequest.getFileNames();
				MultipartFile multipartFile = multipartRequest.getFile(it.next());
				String bookImageUrl = s3Service.storeBookImage(multipartFile, book.getId().toString());
				book.setBookImageUrl(bookImageUrl);
				bookService.save(book);
				return new ResponseEntity("Update Success!", HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Update failed!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping("/bookList")
	public List<Book> getBookList() {
		return bookService.findAll();
	}
	
	@RequestMapping(value="/update", method= RequestMethod.POST)
	public Book updateBookPost(@RequestBody Book book) {
		return bookService.save(book);
	}
	
	@RequestMapping(value="/remove", method= RequestMethod.POST)
	public ResponseEntity remove(@RequestBody String id) throws IOException {
		s3Service.deleteImageFromS3(id);
		bookService.removeOne(Long.parseLong(id));
		
		return new ResponseEntity("Remove Success!", HttpStatus.OK);
	}
	
	@RequestMapping("/{id}")
	public Book getBook(@PathVariable("id") Long id){
		Book book = bookService.findOne(id);
		return book;
	}
	
	@RequestMapping(value="/searchBook", method= RequestMethod.POST)
	public List<Book> searchBook (@RequestBody String keyword) {
		List<Book> bookList = bookService.blurrySearch(keyword);
		
		return bookList;
	}
}
