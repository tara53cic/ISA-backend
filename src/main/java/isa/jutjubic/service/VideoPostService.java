package isa.jutjubic.service;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoPostService {
    VideoPost create(User author, VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video);
    List<VideoPost> findAll();
    VideoPost findById(Long id);
}
