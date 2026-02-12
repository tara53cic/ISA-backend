package isa.jutjubic.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VideoPostCreateRequest {
    private String title;
    private String description;
    private List<String> tags;
    private String location;

    private LocalDateTime scheduledAt;


    public VideoPostCreateRequest() {
        this.tags = new ArrayList<>();
    }

    public VideoPostCreateRequest( String title, String description, List<String> tags, String location)
    {
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.location = location;
    }

    public String getTitle() { return title; }

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

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

}
