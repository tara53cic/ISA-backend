package isa.jutjubic.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VideoPostDetails {
    public Long id;
    public String title;
    public String description;
    public List<String> tags;
    public LocalDateTime createdAt;
    public String videoUrl;

    public VideoPostDetails()
    {
        this.tags = new ArrayList<>();
    }

    public VideoPostDetails(Long id, String title, String description, List<String> tags, LocalDateTime createdAt, String videoUrl)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.createdAt = createdAt;
        this.videoUrl = videoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
