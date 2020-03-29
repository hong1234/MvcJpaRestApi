package com.hong.demo.repository;

import org.springframework.data.repository.CrudRepository;
import com.hong.demo.domain.Book;

public interface BookRepository extends CrudRepository<Book, Integer> {

}
