package com.example.ignite.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {

    private List<Book> books;
    private int amountOfPages;

}
