package com.example.ignite.repository;


import com.example.ignite.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import static org.assertj.core.api.Java6Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;



    @Test
    public void testToSeePosts() {

        Book book = new Book("Varia", "Test Book", "William Shakespeare", "1234");
        entityManager.persist(book);

        Book found = bookRepository.findById(book.getId()).get();

        assertThat(found.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void testSearchPosts() {


        Book book = new Book("Varia", "Test Book", "William Shekspeare", "1234");
        entityManager.persist(book);
        entityManager.flush();
        book = new Book("Varia", "Test Book2", "Ernest Hemingway", "01234B");
        entityManager.persist(book);
        entityManager.flush();

        Page found = bookRepository.findBySearchTerm("2", Pageable.unpaged());
        assertThat(found.getTotalElements()).isEqualTo(1);

        found = bookRepository.findBySearchTerm("Test Book", Pageable.unpaged());
        assertThat(found.getTotalElements()).isEqualTo(2);
    }

}