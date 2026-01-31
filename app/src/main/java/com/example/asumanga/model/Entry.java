package com.example.asumanga.model;

public class Entry {
    public enum Type { MANGA, NOVEL }
    private String title;
    private String author;
    private int totalChapters;
    private int currentChapter;
    private int rating;
    private String description;
    private String coverPath;
    private Type type;

    public Entry(String title, String author, int totalChapters, int currentChapter, int rating, String description, Type type) {
        this.title = title;
        this.author = author;
        this.totalChapters = totalChapters;
        this.currentChapter = currentChapter;
        this.rating = rating;
        this.description = description;
        this.type = type;
    }

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

    public int getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(int totalChapters) {
        this.totalChapters = totalChapters;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
