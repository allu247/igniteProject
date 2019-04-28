package com.example.ignite.service;


import com.example.ignite.entity.Comment;
import com.example.ignite.entity.CommentInput;
import com.example.ignite.repository.BookRepository;
import com.example.ignite.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTests {

    @MockBean
    private BookRepository bookRepository;


    @MockBean
    private CommentRepository commentRepository;

    private CommentService commentService;


    private long id_five;


    @Before
    public void setUp() {
        id_five = 5L;


        commentService = new CommentService(commentRepository, bookRepository);
        Comment fullComment = new Comment(12L, "test", new Date(), new Date(), null);


        Mockito.when(commentRepository.save(any(Comment.class))).thenReturn(fullComment);


    }

    @Test
    public void testSaveReply() {
        CommentInput commentInput = new CommentInput("test", 10L);
        Comment savedComment = commentService.save(commentInput);

        assertThat(savedComment.getComment()).isNotNull();
        assertEquals("test", savedComment.getComment());
    }



}
