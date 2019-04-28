package com.example.ignite.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentInput {

    @NotNull
    @NotEmpty
    @Size(min = 5)
    private String comment;

    @NotNull
    private Long bookId;


}
