package isa.jutjubic.controller;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.service.UserService;
import isa.jutjubic.service.VideoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/videos", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class VideoPostController {

    @Autowired
    private VideoPostService videoService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VideoPost> createPost(
            @RequestPart("data") VideoPostCreateRequest request,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("video") MultipartFile video,
            Principal principal) throws IOException
    {
        User author = userService.findByUsername(principal.getName());
        VideoPost created = videoService.createPost(request, thumbnail, video, author);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<VideoPost>> getVideos()
    {
        return ResponseEntity.ok(videoService.getVideos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoPost> getVideo(@PathVariable Long id)
    {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @GetMapping(value = "/{id}/thumbnail", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) throws IOException
    {
        byte[] thumbnail = videoService.getThumbnail(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG).cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic())
                .body(thumbnail);
    }

    @GetMapping(value = "/{id}/watch")
    public ResponseEntity<Resource> watchVideo(@PathVariable Long id) {
        try {
            VideoPost post = videoService.getVideoById(id);

            Path path = Paths.get(post.getVideoPath());

            Resource video = new UrlResource(path.toUri());

            if (!video.exists() || !video.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "video/mp4";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(video);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<?> incrementView(@PathVariable Long id) {
        videoService.recordView(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/view/local")
    public long getLocalViews(@PathVariable Long id) {
        return videoService.getLocalViewCount(id);
    }

    @PostMapping("/{id}/merge")
    public void merge(
            @PathVariable Long id,
            @RequestBody Map<String, Long> otherState
    ) {
        videoService.merge(id, otherState);
    }

    @GetMapping("/{id}/state")
    public Map<String, Long> getState(@PathVariable Long id) {
        return videoService.getCounter(id).getState();
    }

    @GetMapping("/{id}/stream-info")
    public ResponseEntity<?> getStreamInfo(@PathVariable Long id) {
        VideoPost post = videoService.getVideoById(id);

        if (post.getScheduledAt() == null) {
            return ResponseEntity.ok(Map.of("type", "VOD", "offset", 0));
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(post.getScheduledAt())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Video hasn't started yet");
        }

        // Calculate seconds passed since start
        long secondsPassed = java.time.Duration.between(post.getScheduledAt(), now).toSeconds();

        // If the video is 10 mins long and 12 mins passed, the "stream" is over
        if (post.getDuration() != null && secondsPassed > post.getDuration()) {
            return ResponseEntity.ok(Map.of("type", "VOD_FINISHED", "offset", 0));
        }

        return ResponseEntity.ok(Map.of(
                "type", "LIVE_SIMULATION",
                "offset", secondsPassed
        ));
    }

}
