package isa.jutjubic.service;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoPostService {
    VideoPost createPost(VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video, User author) throws IOException;

    List<VideoPost> getVideos();

    VideoPost getVideoById(Long id);

    byte[] getThumbnail(Long id) throws IOException;
}
