package isa.jutjubic.repository;

import isa.jutjubic.model.VideoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoPostRepository extends JpaRepository<VideoPost, Long> {
    List<VideoPost> findAllByOrderByCreatedAtDesc();
}
