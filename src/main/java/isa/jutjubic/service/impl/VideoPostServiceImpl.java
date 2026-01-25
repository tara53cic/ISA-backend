package isa.jutjubic.service.impl;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.repository.VideoPostRepository;
import isa.jutjubic.service.VideoPostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VideoPostServiceImpl implements VideoPostService {

    @Autowired
    private VideoPostRepository repository;

    @Value("${app.storage.videos-dir:storage/videos}")
    private String videosDir;

    @Value("${app.storage.thumbs-dir:storage/thumbs}")
    private String thumbsDir;

    @Value("${app.upload.timeout-seconds:30}")
    private long uploadTimeoutSeconds;

    @Override
    @Transactional
    public VideoPost createPost(VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video)
    {

    }
}
