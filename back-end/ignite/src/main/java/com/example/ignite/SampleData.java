package com.example.ignite;



import com.example.ignite.entity.Book;
import com.example.ignite.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

import java.util.stream.Stream;

@Component
public class SampleData implements CommandLineRunner {




    private final BookRepository bookRepository;


    public SampleData(BookRepository bookRepository) {
        this.bookRepository = bookRepository;

    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {



        Stream.of(
                Arrays.asList("Mathematics", "Mathematics1", "Jon Snow", "02243"),
                Arrays.asList("Varia","Biology1", "Alvar Valtna", "55468")

       )
                .forEach(title -> bookRepository.save(
                        new Book(title.get(0), title.get(1), title.get(2), title.get(3))
                ));


    }
}
