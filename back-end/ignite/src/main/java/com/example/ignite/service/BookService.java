package com.example.ignite.service;



import com.example.ignite.entity.Book;
import com.example.ignite.entity.BookListResponse;
import com.example.ignite.exceptions.BadRequestException;
import com.example.ignite.exceptions.PageNotFoundException;
import com.example.ignite.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    /**
     * Get methods to receive posts.
     */



    public Book getBookItemById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        } else {
            throw new PageNotFoundException("Sorry, this page does not exist!");
        }
    }

    public BookListResponse getBooks(int page, int size, String topic, String order, String sortBy) {
        Pageable pageableRequest = getPageable(page, size, order, sortBy);

        Page<Book> books;
        if (topic.equals("all")) {
            books = bookRepository.findAll(pageableRequest);
        } else {
            books = bookRepository.findAllByTopic(topic, pageableRequest);
        }

        return getBooksResponse(books, page);
    }

    public BookListResponse findBooks(int page, int size, String searchTerm, String order, String sortBy) {
        Pageable pageableRequest = getPageable(page, size, order, sortBy);
        Page<Book> books = bookRepository.findBySearchTerm(searchTerm, pageableRequest);

        return getBooksResponse(books, page);
    }

    private BookListResponse getBooksResponse(Page<Book> books, int page) {
        if (page > books.getTotalPages()) {
            throw new PageNotFoundException("This page does not exist!");
        }

        return new BookListResponse(books.getContent(), books.getTotalPages());
    }

    private Pageable getPageable(int page, int size, String order, String sortBy) {
        Pageable pageableRequest = null;

        if (order.equals("ascending")) {
            pageableRequest = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageableRequest = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        return pageableRequest;
    }


    public Book saveBook(Book input) {

        return bookRepository.save(input);
    }


    public Book patchBook(Book obj, Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);

        System.out.println(book.toString());
        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            bookToUpdate.setTopic(obj.getTopic());
            bookToUpdate.setTitle(obj.getTitle());
            bookToUpdate.setAuthor(obj.getAuthor());
            bookToUpdate.setISBN(obj.getISBN());
            return bookRepository.save(bookToUpdate);
        }

        throw new BadRequestException("Problem updating Your book!");
    }


    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }


}
