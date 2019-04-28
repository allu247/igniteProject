package com.example.ignite.service;



import com.example.ignite.entity.Book;
import com.example.ignite.entity.Comment;
import com.example.ignite.entity.CommentInput;
import com.example.ignite.exceptions.BadRequestException;
import com.example.ignite.repository.BookRepository;
import com.example.ignite.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final BookRepository bookRepo;


    public CommentService(CommentRepository commentRepo, BookRepository bookRepo) {
        this.commentRepo = commentRepo;
        this.bookRepo = bookRepo;

    }

    public Comment save(CommentInput input) {
        Comment item = new Comment();
        Optional<Book> relatedBook = bookRepo.findById(input.getBookId());
        if (relatedBook.isPresent()) {
            item.setComment(input.getComment());
            item.setBook(relatedBook.get());

        }
        return commentRepo.save(item);
    }

    public void delete(Long id) {

        commentRepo.deleteComment(id);
    }

    public Comment patch(CommentInput obj, Long id) {
        Optional<Comment> comment = commentRepo.findById(id);

        if (comment.isPresent()) {

             commentRepo.updateReply(id, obj.getComment());
        }

        throw new BadRequestException("Problem updating Your comment!");
    }


}
