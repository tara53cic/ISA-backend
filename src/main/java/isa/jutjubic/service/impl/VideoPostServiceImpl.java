package isa.jutjubic.service.impl;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.repository.VideoPostRepository;
import isa.jutjubic.service.VideoPostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

public class VideoPostServiceImpl implements VideoPostService {

    private VideoPostRepository repository;

    @Value("${app.storage.videos-dir:storage/videos}")
    private String videosDir;

    @Value("${app.storage.thumbs-dir:storage/thumbs}")
    private String thumbsDir;

    @Value("${app.upload.timeout-seconds:30}")
    private long uploadTimeoutSeconds;

    @Override
    public List<VideoPost> findAll()
    {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public VideoPost findById(Long id)
    {
        return this.repository.findById(id).orElseGet(null);
    }

    /*public VideoPost create(User user, VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile videoPath)
    {

    }*/
}
