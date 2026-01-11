package isa.jutjubic.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "VIDEO_POSTS")
public class VideoPost {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "videoPostTags", joinColumns = @JoinColumn(name = "videoPostId"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "videoPath", nullable = false)
    private String videoPath;

    @Column(name = "createdAt")
    private Instant createdAt;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private User author;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void addTags(String tag)
    {
        this.tags.add(tag);
    }

    public void removeTag(String tag)
    {
        this.tags.remove(tag);
    }

    public boolean isAuthor(User user)
    {
        return user != null && this.author != null && this.author.getId().equals(user.getId());
    }
}
