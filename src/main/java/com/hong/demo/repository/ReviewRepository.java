package com.hong.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.hong.demo.domain.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Query("from Review r where r.book.id=?1")
    List<Review> getReviewsOfBook(Integer bookId);
}
