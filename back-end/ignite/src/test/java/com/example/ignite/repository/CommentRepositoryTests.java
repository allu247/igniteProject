package com.example.ignite.repository;



import com.example.ignite.entity.Book;
import com.example.ignite.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import static org.assertj.core.api.Java6Assertions.assertThat;

@DataJpaTest
public class CommentRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testToSeeReplies() {


        Book book = new Book("Varia", "Test Book", "William Shakespeare", "1234");


        entityManager.persist(book);
        entityManager.flush();

        Comment comment = new Comment("testComment", book);
        entityManager.persist(comment);
        entityManager.flush();

        Comment found = commentRepository.findById(comment.getId()).get();

        assertThat(found.getComment()).isEqualTo(comment.getComment());
    }

}