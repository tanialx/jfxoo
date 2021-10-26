package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.annotation.JFXooForm;

import java.math.BigDecimal;
import java.time.LocalDate;

@JFXooForm
public class Book {
    private String title;
    private String author;
    private LocalDate publishedDate;
    private BigDecimal price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
