package com.example.ignite.controller;




import com.example.ignite.entity.Book;
import com.example.ignite.entity.BookListResponse;
import com.example.ignite.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



// PS! BookListResponse holds the response and the amount of pages!

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("api/books/{id}")
    public Book getPostItemById(@PathVariable Long id) {
        return bookService.getBookItemById(id);
    }

    @GetMapping("api/books")
    public BookListResponse getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "15") int size,
                                     @RequestParam(value = "topic", defaultValue = "all") String topic,
                                     @RequestParam(value = "order", defaultValue = "ascending") String order,
                                     @RequestParam(value = "sortBy", defaultValue = "postedAt") String sortBy) {
        if (topic.equals("home")) {
            topic = "all";
        }
        return bookService.getBooks(page, size, topic, order, sortBy);
    }

    @GetMapping("api/books/find")
    public BookListResponse findPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "15") int size,
                                      @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
                                      @RequestParam(value = "order", defaultValue = "ascending") String order,
                                      @RequestParam(value = "sortBy", defaultValue = "postedAt") String sortBy) {
        return bookService.findBooks(page, size, searchTerm, order, sortBy);
    }

    @PostMapping("api/add/book")
    public Book savePost(@RequestBody @Valid Book item) {
        return bookService.saveBook(item);
    }

    @DeleteMapping("api/delete/book/{id}")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PatchMapping("api/edit/book/{id}")
    public Book patchPost(@RequestBody @Valid Book obj, @PathVariable Long id) {
        return bookService.patchBook(obj, id);
    }

}

