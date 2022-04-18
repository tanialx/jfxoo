package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.annotation.JFXooForm;
import io.github.tanialx.jfxoo.annotation.JFXooTable;
import io.github.tanialx.jfxoo.annotation.JFXooVar;
import io.github.tanialx.jfxoo.annotation.JFXooVarType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JFXooForm
@JFXooTable
public class Book {
    private String title;
    private String author;
    @JFXooVar(label = "Published on")
    private LocalDate publishedDate;
    private BigDecimal price;
    @JFXooVar(type = JFXooVarType.textarea)
    private String summary;
    @JFXooVar(label = "Public domain")
    private Boolean isInPublicDomain;
    private List<Review> reviews;

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

    public List<Review> getReviews() { return reviews; }

    public void setReviews(List<Review> reviews) { this.setReviews(reviews); }
}
