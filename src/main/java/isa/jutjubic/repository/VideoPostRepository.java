package isa.jutjubic.repository;

import isa.jutjubic.model.VideoPost;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface VideoPostRepository extends JpaRepository<VideoPost, Long>
{
    @Modifying
    @Transactional
    @Query("update VideoPost v set v.views = v.views + 1 where v.id = :id")
    int incrementViewCount(@Param("id") Long id);

    @Query("SELECT v FROM VideoPost v WHERE v.scheduledAt IS NULL OR v.scheduledAt <= :now")
    List<VideoPost> findAllVisible(@Param("now") LocalDateTime now, Sort sort);
}
