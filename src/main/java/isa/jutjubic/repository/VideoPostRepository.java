package isa.jutjubic.repository;

import isa.jutjubic.model.VideoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VideoPostRepository extends JpaRepository<VideoPost, Long>
{
    @Modifying
    @Transactional
    @Query("update VideoPost v set v.views = v.views + 1 where v.id = :id")
    int incrementViewCount(@Param("id") Long id);
}
