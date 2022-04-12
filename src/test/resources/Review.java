package io.github.tanialx.jfxoo.test;

import io.github.tanialx.jfxoo.annotation.JFXooForm;
import io.github.tanialx.jfxoo.annotation.JFXooTable;

@JFXooTable
public class Review {
    private Integer score;
    private String comment;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}