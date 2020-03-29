package com.hong.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import com.hong.demo.domain.Review;
import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Query("from Review r where r.book.id=?1")
    List<Review> getReviewsOfBook(Integer bookId);
}
