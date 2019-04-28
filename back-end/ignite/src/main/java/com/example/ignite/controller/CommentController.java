package com.example.ignite.controller;


import com.example.ignite.entity.Comment;

import com.example.ignite.entity.CommentInput;
import com.example.ignite.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("api/add/comment")
    public Comment save(@RequestBody @Valid CommentInput item) {

        return commentService.save(item);
    }

    @DeleteMapping("api/delete/comment/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }

    @PatchMapping("api/edit/comment/{id}")
    public void patch(@RequestBody @Valid CommentInput obj, @PathVariable Long id) {

        commentService.patch(obj, id);
    }

}