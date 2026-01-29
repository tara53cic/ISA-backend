package isa.jutjubic.service.impl;

import isa.jutjubic.crdt.GCounter;
import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.repository.VideoPostRepository;
import isa.jutjubic.service.VideoPostService;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class VideoPostServiceImpl implements VideoPostService {

    @Autowired
    private VideoPostRepository repository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${app.storage.videos-dir:storage/videos}")
    private String videosDir;

    @Value("${app.storage.thumbs-dir:storage/thumbs}")
    private String thumbsDir;

    @Value("${replica.id:default}")
    private String replicaId;

    private final Map<Long, GCounter> viewCounters = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public VideoPost createPost(VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video, User author) throws IOException
    {
        Files.createDirectories(Paths.get(videosDir));
        Files.createDirectories(Paths.get(thumbsDir));

        Path videoPath = buildPath(videosDir, video.getOriginalFilename());
        Path thumbnailPath = buildPath(thumbsDir, thumbnail.getOriginalFilename());

        try {
            Files.copy(video.getInputStream(), videoPath);
            Files.copy(thumbnail.getInputStream(), thumbnailPath);

            VideoPost post = new VideoPost();
            post.setTitle(request.getTitle());
            post.setDescription(request.getDescription());
            post.setTags(request.getTags());
            post.setLocation(request.getLocation());
            post.setThumbnail(thumbnailPath.toString());
            post.setVideoPath(videoPath.toString());
            post.setAuthor(author);

            return repository.save(post);
        }
        catch(Exception e) {
            Files.deleteIfExists(videoPath);
            Files.deleteIfExists(thumbnailPath);
            throw e;
        }
    }

    private Path buildPath(String dir, String original)
    {
        String safeName = (original == null || original.isBlank()) ? "file" : original;
        String fileName = UUID.randomUUID() + "_" + safeName;
        return Paths.get(dir, fileName);
    }

    @Override
    public List<VideoPost> getVideos()
    {
        return repository.findAll(Sort.by(DESC, "createdAt"));
    }

    @Override
    public VideoPost getVideoById(Long id)
    {
        repository.incrementViewCount(id);
        VideoPost video = repository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        this.messagingTemplate.convertAndSend("/topic/videos/" + id, video);
        return video;
    }

    @Override
    @Cacheable(value = "thumbnails", key = "#id")
    public byte[] getThumbnail(Long id) throws IOException
    {
        VideoPost post = repository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
        return Files.readAllBytes(Paths.get(post.getThumbnail()));
    }

    @Override
    public void recordView(Long id) {
        GCounter counter = getCounter(id);
        counter.increment(replicaId);

    }


    @Override
    public GCounter getCounter(Long videoId) {
        return viewCounters.computeIfAbsent(videoId, id -> new GCounter());
    }

    @Override
    public long getLocalViewCount(Long videoId) {
        return getCounter(videoId).value();
    }

    @Override
    public void merge(Long videoId, Map<String, Long> otherState) {
        GCounter incoming = new GCounter();
        otherState.forEach((replica, value) ->
                incoming.getState().put(replica, value)
        );

        GCounter local = getCounter(videoId);
        local.merge(incoming);

        // SNAPSHOT posle merge-a
        persistViewsSnapshot(videoId);
    }


    @Override
    public void persistViewsSnapshot(Long videoId) {
        long value = getCounter(videoId).value();

        VideoPost video = repository.findById(videoId).orElseThrow();
        video.setViews(value);
        repository.save(video);
    }

}
