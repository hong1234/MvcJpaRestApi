package com.hong.demo.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hong.demo.domain.Review;
import com.hong.demo.domain.Book;
import com.hong.demo.repository.ReviewRepository;
import com.hong.demo.repository.BookRepository;

import com.hong.demo.validation.ValidationError;
import com.hong.demo.validation.ValidationErrorBuilder;

import com.hong.demo.exceptions.BookDeletionException;
import com.hong.demo.exceptions.ResourceNotFoundException;

import org.springframework.validation.Errors;
import com.hong.demo.validation.ValidationError;
import com.hong.demo.validation.ValidationErrorBuilder;
import com.hong.demo.exceptions.ErrorDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.persistence.EntityNotFoundException;
import java.lang.IllegalArgumentException;


@RestController
@RequestMapping(value="/books")
public class BookController
{
    @Autowired
    BookRepository bookRepository;
    
    @Autowired
    ReviewRepository reviewRepository;

    @GetMapping("")
    public ResponseEntity<Iterable<Book>> listBooks()
    {
	return ResponseEntity.ok(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Integer id)
    {
	Optional<Book> book = bookRepository.findById(id);
      
        if(book.isPresent())
         return ResponseEntity.ok(book.get());

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{bookId}/reviews")
    public List<Review> getReviewsOfBook(@PathVariable("bookId") Integer bookId)
    {
    	return reviewRepository.getReviewsOfBook(bookId);
    }

    @PostMapping("")
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, Errors errors)
    {
	if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }

	Book savedBook = bookRepository.save(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedBook.getId()).toUri();
        return ResponseEntity.created(location).body(savedBook);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Integer id, @Valid @RequestBody  Book book, Errors errors)
    {
    	if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }

        Optional<Book> result = bookRepository.findById(id);      
        
	if(!result.isPresent())
	    return ResponseEntity.notFound().build();

        Book b = result.get();
	b.setTitle(book.getTitle());
	b.setContent(book.getContent());

        Book updatedBook = bookRepository.save(b);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(updatedBook.getId()).toUri();
        return ResponseEntity.created(location).body(updatedBook);
        
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable("id") Integer id)
    {
        Optional<Book> book = bookRepository.findById(id);
	if(!book.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();      
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> createBookReview(@PathVariable("id") Integer id, @Valid @RequestBody Review review, Errors errors)
    {
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }

	Optional<Book> book = bookRepository.findById(id);
        if(!book.isPresent())
            return ResponseEntity.notFound().build();

	review.setBook(book.get());
        book.get().getReviews().add(review);
        Review savedReview = reviewRepository.save(review);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedReview.getId()).toUri();
        return ResponseEntity.created(location).body(savedReview);  
    }
    
    @DeleteMapping("/{bookId}/reviews/{reviewId}")
    public ResponseEntity<Review> deleteBookReview(@PathVariable("bookId") Integer bookId, @PathVariable("reviewId") Integer reviewId)
    {
        Optional<Review> review = reviewRepository.findById(reviewId);
	
	if(!review.isPresent())
	    return ResponseEntity.notFound().build(); 

	reviewRepository.deleteById(review.get().getId());
        return ResponseEntity.noContent().build();
    }
    

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        //log.error(exception.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(); 
        errorDetails.setErrorMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errorDetails);
    }

}
