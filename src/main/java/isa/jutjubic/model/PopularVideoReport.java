package isa.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PopularVideoReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime executionTime;

    @OneToMany(cascade = CascadeType.ALL)
    private List<VideoPopularityEntry> topVideos;

    public PopularVideoReport(List<VideoPopularityEntry> topVideos) {
        this.executionTime = LocalDateTime.now();
        this.topVideos = topVideos;
    }
}
