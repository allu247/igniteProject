package com.example.ignite.repository;

import com.example.ignite.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;


public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.comment =:comment WHERE c.id =:id")
    void updateReply(@Param("id") Long id, @Param("comment") String comment);


    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.id =:id")
    void deleteComment(@Param("id") Long id);
}
