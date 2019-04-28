package com.example.ignite.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String topic;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String title;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    private String author;

    @NotNull
    @NotEmpty
    @Size(min = 3)
    @Column(name = "ISBN")
    private String ISBN;

    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name = "posted_at")
    private Date postedAt;

    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new LinkedList<>();


    @PrePersist
    protected void onCreate() {
        postedAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public Book(String topic,String title, String author, String ISBN) {

        this.title = title;
        this.topic = topic;
        this.author = author;
        this.ISBN = ISBN;

    }




}
