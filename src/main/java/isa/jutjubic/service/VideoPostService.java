package isa.jutjubic.service;

import isa.jutjubic.crdt.GCounter;
import isa.jutjubic.dto.VideoPostCreateRequest;
import isa.jutjubic.model.User;
import isa.jutjubic.model.VideoPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface VideoPostService {

    VideoPost createPost(VideoPostCreateRequest request, MultipartFile thumbnail, MultipartFile video, User author) throws IOException;

    List<VideoPost> getVideos();

    VideoPost getVideoById(Long id);

    byte[] getThumbnail(Long id) throws IOException;

    void recordView(Long id);
    GCounter getCounter(Long videoId);
    long getLocalViewCount(Long videoId);
    void merge(Long videoId, Map<String, Long> otherState);
    void persistViewsSnapshot(Long videoId);
}
