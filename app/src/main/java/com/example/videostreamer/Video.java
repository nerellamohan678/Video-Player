package com.example.videostreamer;

import java.io.Serializable;

//to store the extracted values.
public class Video implements Serializable {
    private String title;
    private String description;
    private String author;
    private String videoUrl;
    private String imageUrl;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        //we have to change the link to https as it wont display if it is in http.
        imageUrl =  imageUrl.substring(0, 4) + "s" + imageUrl.substring(4, imageUrl.length());
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        videoUrl = videoUrl.substring(0,4) + "s" + videoUrl.substring(4,videoUrl.length());
        this.videoUrl = videoUrl;
    }
}
