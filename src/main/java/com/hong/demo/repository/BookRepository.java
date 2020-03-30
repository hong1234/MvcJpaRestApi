package com.hong.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.hong.demo.domain.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {
    @Query("from Book b where b.title like %?1%")
    List<Book> searchByTitle(String title);
}
