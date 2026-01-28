package isa.jutjubic.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VIDEO_POSTS")
public class VideoPost {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "videoPostTags", joinColumns = @JoinColumn(name = "videoPostId"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "videoPath")
    private String videoPath;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(name = "views")
    private Long views = 0L;

    public VideoPost()
    {
        this.createdAt = LocalDateTime.now();
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void addTags(String tag)
    {
        this.tags.add(tag);
    }

    public void removeTag(String tag)
    {
        this.tags.remove(tag);
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public User getAuthor() { return author; }

    public void setAuthor(User author) { this.author = author; }

    public Long getViews() { return views; }

    public void setViews(Long views) { this.views = views; }
}
