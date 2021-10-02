package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.annotation.JFXooForm;

import java.math.BigDecimal;
import java.time.LocalDate;

@JFXooForm
public class Book {
    private String title;
    private String author;
    private LocalDate published;
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

    public LocalDate getPublished() {
        return published;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
