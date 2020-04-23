package com.hong.demo.controller;

import java.util.List;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hong.demo.domain.Review;
import com.hong.demo.domain.Book;
import com.hong.demo.repository.ReviewRepository;
import com.hong.demo.repository.BookRepository;

import com.hong.demo.service.BookService;
import com.hong.demo.exceptions.ResourceNotFoundException;
import com.hong.demo.exceptions.ErrorDetails;


@RestController
@RequestMapping(value="/api/books")
public class BookController
{
    @Autowired
    BookService bookService;

    @GetMapping("")
    public ResponseEntity<Iterable<Book>> listBooks()
    {
	    return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable("bookId") Integer bookId)
    {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/search")
    public ResponseEntity<Iterable<Book>> searchBooksByTitle(@RequestParam String title)
    {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    @GetMapping("/{bookId}/reviews")
    public List<Review> getReviewsOfBook(@PathVariable("bookId") Integer bookId)
    {
    	return bookService.getBookReviews(bookId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("")
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, BindingResult errors)
    {
        if(errors.hasErrors())
            return ResponseEntity.badRequest().body(createErrorString(errors));
	    Book savedBook = bookService.storeBook(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBook.getId()).toUri();
        return ResponseEntity.created(location).body(savedBook);
    }
    
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable("bookId") Integer bookId, @Valid @RequestBody  Book book, BindingResult errors)
    {
    	if(errors.hasErrors())
            return ResponseEntity.badRequest().body(createErrorString(errors));
        Book updatedBook = bookService.updateBook(bookId, book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(updatedBook.getId()).toUri();
        return ResponseEntity.created(location).body(updatedBook);
        
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<?> createBookReview(@PathVariable("bookId") Integer bookId, @Valid @RequestBody Review review, BindingResult errors)
    {
        if(errors.hasErrors())
	        return ResponseEntity.badRequest().body(createErrorString(errors));
        Review savedReview = bookService.addReviewToBook(bookId, review);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedReview.getId()).toUri();
        return ResponseEntity.created(location).body(savedReview);  
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ErrorDetails> deleteBook(@PathVariable("bookId") Integer bookId)
    {
        return new ResponseEntity<ErrorDetails>(bookService.deleteBook(bookId), HttpStatus.OK);
    }
    
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ErrorDetails> deleteBookReview(@PathVariable("reviewId") Integer reviewId)
    {
        return new ResponseEntity<ErrorDetails>(bookService.deleteReview(reviewId), HttpStatus.OK);
    }
    
    @ExceptionHandler
    public ResponseEntity<?> handleException(RuntimeException e) {
        ErrorDetails errorDetails = new ErrorDetails(); 
        errorDetails.setErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler
    public ResponseEntity<?> notFoundException(ResourceNotFoundException e){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(HttpStatus.NOT_FOUND);
        errorDetails.setErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String createErrorString(BindingResult result) {
        StringBuilder sb =  new StringBuilder();
        result.getAllErrors().forEach(error -> {
            if(error instanceof FieldError) {
                FieldError err= (FieldError) error;
                sb.append("Field '").append(err.getField()).append("' value error: ").append(err.getDefaultMessage()).append("\n");
            }
        });
        return sb.toString();
    }
    
}
