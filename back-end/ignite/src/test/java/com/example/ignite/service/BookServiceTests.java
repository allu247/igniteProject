package com.example.ignite.service;





import com.example.ignite.entity.Book;
import com.example.ignite.entity.BookListResponse;
import com.example.ignite.exceptions.BadRequestException;
import com.example.ignite.exceptions.PageNotFoundException;
import com.example.ignite.repository.BookRepository;
import com.example.ignite.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;


import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTests {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CommentRepository commentRepository;

    private BookService bookService;

    private Book fullBook;
    private Book fullBook2;


    @Before
    public void setUp() {

        fullBook = new Book("Varia", "First Book", "Hemingway", "00001");
        fullBook2 = new Book("Mathematics", "Second Book", "Huxley", "00002");
        bookService = new BookService(bookRepository);
        bookRepository.save(fullBook);
        bookRepository.save(fullBook2);
        Mockito.when(bookRepository.save(any(Book.class))).thenReturn(fullBook);
        Mockito.when(bookRepository.findById(0L)).thenReturn(Optional.of(fullBook));
        Mockito.when(bookRepository.findAllByTopic(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(fullBook)));
        Mockito.when(bookRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(fullBook, fullBook2)));
        Mockito.when(bookRepository.findBySearchTerm(any(String.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(fullBook2)));


    }

    @Test
    public void testSavePost() {

        Book fullBook3 = new Book("Varia", "Third Book", "Thompson", "00003");
        Book savedBook = bookService.saveBook(fullBook3);

        assertThat(savedBook.getTitle()).isNotNull();
        assertEquals("First Book", savedBook.getTitle());
    }

    @Test(expected = PageNotFoundException.class)
    public void testTryToFindPostByID() {
        bookService.getBookItemById(3L);
    }

    @Test
    public void testFindPostByID() {
        Book book = bookService.getBookItemById(0L);
        assertEquals("First Book", book.getTitle());
    }

    @Test
    public void testFindPostByTopic() {
        int page = 0;
        int size = 10;
        String topic = "Varia";
        String order = "ascending";
        String sortBy = "title";

        BookListResponse response = bookService.getBooks(page, size, topic, order, sortBy);

        assertEquals(1, response.getAmountOfPages());
        assertEquals("First Book", response.getBooks().get(0).getTitle());
    }

    @Test
    public void testFindAllPosts() {
        int page = 0;
        int size = 10;
        String topic = "all";
        String order = "ascending";
        String sortBy = "title";

        BookListResponse response = bookService.getBooks(page, size, topic, order, sortBy);

        assertEquals(1, response.getAmountOfPages());
        assertEquals("First Book", response.getBooks().get(0).getTitle());
    }

    @Test
    public void testFindThroughSearch() {
        int page = 0;
        int size = 10;
        String searchTerm = "sec";
        String order = "ascending";
        String sortBy = "title";

        BookListResponse response = bookService.findBooks(page, size, searchTerm, order, sortBy);

        assertEquals(1, response.getAmountOfPages());
        assertEquals("Second Book", response.getBooks().get(0).getTitle());
    }

    @Test(expected = BadRequestException.class)
    public void testTryToPatchPost() {
        bookService.patchBook(new Book("Varia", "Edited Book", "Hemingway", "00001"), 3L);
    }

    @Test
    public void testPatchPost() {


        Book response = bookService.patchBook(
                new Book("Varia", "Edited Book", "Hemingway", "00001"), 0L
        );
        assertEquals("Edited Book", response.getTitle());
    }
}
