package isa.jutjubic.service;

import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.dto.VideoPostDetails;
import isa.jutjubic.dto.VideoPostList;
import isa.jutjubic.model.VideoPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoPostService {
    VideoPost createPost(VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video);

    List<VideoPostList> getVideos();

    VideoPostDetails getVideoDetails(Long id);

    byte[] getThumbnail(Long id);
}
