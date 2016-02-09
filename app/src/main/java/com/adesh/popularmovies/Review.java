package com.adesh.popularmovies;

/**
 * Created by adesh on 9/2/16.
 */
public class Review {
    private String author, content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
