package com.example.cgi;


import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

import java.util.stream.Stream;

@Component
public class SampleData implements CommandLineRunner {




    public SampleData() {


    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {





    }
}
