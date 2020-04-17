package com.hong.demo.service;

import com.hong.demo.domain.Book;
import com.hong.demo.domain.Review;
import com.hong.demo.exceptions.ResourceNotFoundException;
import com.hong.demo.repository.BookRepository;
import com.hong.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
//import java.util.Optional;

import com.hong.demo.exceptions.ErrorDetails;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ReviewRepository reviewRepository;

    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer bookId) //throws Exception
    {
        //Optional<Book> book = bookRepository.findById(bookId);
        //if(!book.isPresent())
        //    throw new ResourceNotFoundException("Book with Id="+bookId+" not found");
        //return book.get();

        return bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with Id="+bookId+" not found"));
    }

    public Iterable<Book> searchBooksByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    public List<Review> getBookReviews(Integer bookId){
        return reviewRepository.getReviewsOfBook(bookId);
    }

    public Book storeBook(Book book)
    {
        return bookRepository.save(book);
    }

    public Book updateBook(Integer bookId, Book book)
    {
        Book storedBook = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with Id="+bookId+" not found"));
        storedBook.setTitle(book.getTitle());
        storedBook.setContent(book.getContent());
        storedBook.setUpdatedOn(new Date());
        return bookRepository.save(storedBook);
    }

    public Review addReviewToBook(Integer bookId, Review review)
    {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with Id="+bookId+" not found"));
        review.setBook(book);
        book.getReviews().add(review);
        return reviewRepository.save(review);
    }

    public ErrorDetails deleteBook(Integer bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with Id="+bookId+" not found"));
        bookRepository.deleteById(bookId);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(HttpStatus.OK);
        errorDetails.setErrorMessage("Book Id="+bookId+" deleted");
        return errorDetails;
    }

    public ErrorDetails deleteReview(Integer reviewId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review with Id="+reviewId+" not found"));
        reviewRepository.deleteById(reviewId);
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(HttpStatus.OK);
        errorDetails.setErrorMessage("Review Id="+reviewId+"  deleted");
        return errorDetails;
    }

}
