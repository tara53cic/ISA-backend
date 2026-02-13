package isa.jutjubic.repository;

import isa.jutjubic.model.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {

    long countByVideoId(Long videoId);

    @Query("SELECT v FROM View v WHERE v.viewDate >= :sevenDaysAgo")
    List<View> findRecentViews(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

}

