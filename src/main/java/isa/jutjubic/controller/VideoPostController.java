package isa.jutjubic.controller;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.service.UserService;
import isa.jutjubic.service.VideoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
    
}
