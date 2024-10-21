package com.example.tpo_s23878;

public class Book {
    private int id;
    private String tytuł;
    private String autor;
    private String overview;

    public Book(int id, String tytuł, String autor, String overview){
        this.id = id;
        this.tytuł = tytuł;
        this.autor = autor;
        this.overview = overview;
    }

    @Override
    public String toString(){
        return autor + " - " + tytuł + ": " + overview + "\n";
    }
}
