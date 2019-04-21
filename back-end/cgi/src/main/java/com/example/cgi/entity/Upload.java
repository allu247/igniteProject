package com.example.cgi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Upload {

    @Id
    @GeneratedValue
    private Long id;
    private String filename;
    private String formatting;
    private int height;
    private int width;
    private String downloadUri;
    private String category;



    public Upload(String filename, String formatting,int height, int width, String category) {

        this.filename = filename;
        this.formatting = formatting;
        this.height = height;
        this.width = width;
        this.category = category;

    }



    @Override
    public String toString() {
        return this.filename + " " + this.formatting + " " +this.height + " " + this.width;
    }
}
