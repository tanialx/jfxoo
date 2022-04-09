package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.annotation.JFXooForm;
import io.github.tanialx.jfxoo.annotation.JFXooVar;
import io.github.tanialx.jfxoo.annotation.JFXooVarType;

import java.math.BigDecimal;
import java.time.LocalDate;

@JFXooForm
public class Book {
    private String title;
    private String author;
    private LocalDate publishedDate;
    private BigDecimal price;
    @JFXooVar(type = JFXooVarType.textarea)
    private String summary;
    private Boolean isInPublicDomain;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getIsInPublicDomain() {
        return isInPublicDomain;
    }

    public void setIsInPublicDomain(Boolean inPublicDomain) {
        isInPublicDomain = inPublicDomain;
    }
}
