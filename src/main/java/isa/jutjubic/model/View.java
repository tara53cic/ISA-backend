package isa.jutjubic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "VIDEO_VIEWS")
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Column(name = "view_date", nullable = false)
    private LocalDateTime viewDate;

    public View() {}

    public View(Long videoId) {
        this.videoId = videoId;
        this.viewDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public LocalDateTime getViewDate() {
        return viewDate;
    }

    public void setViewDate(LocalDateTime viewDate) {
        this.viewDate = viewDate;
    }
}

