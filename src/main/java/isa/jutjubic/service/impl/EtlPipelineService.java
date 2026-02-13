package isa.jutjubic.service.impl;

import isa.jutjubic.model.PopularVideoReport;
import isa.jutjubic.model.VideoPopularityEntry;
import isa.jutjubic.model.VideoPost;
import isa.jutjubic.model.View;
import isa.jutjubic.repository.PopularVideoReportRepository;
import isa.jutjubic.repository.VideoPostRepository;
import isa.jutjubic.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtlPipelineService {

    private final ViewRepository viewRepository;
    private final PopularVideoReportRepository reportRepository;
    private final VideoPostRepository videoRepository;
    private static final Logger log = LoggerFactory.getLogger(EtlPipelineService.class);


    //@Scheduled(cron = "0 0 2 * * *") //  2:00 svaki dan
    @Scheduled(cron = "0 * * * * *")
    public void runPopularityPipeline() {

        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7).with(LocalTime.MIN);
        List<View> recentViews = viewRepository.findRecentViews(sevenDaysAgo);
        if (recentViews.isEmpty()) {
            log.warn("No views found for the last 7 days! Transformation skipped.");
            return;
        }


        Map<Long, Double> scores = new HashMap<>();
        LocalDate today = LocalDate.now();

        for (View view : recentViews) {
            long daysAgo = ChronoUnit.DAYS.between(view.getViewDate().toLocalDate(), today);
            double weight = 7 - daysAgo + 1;

            scores.merge(view.getVideoId(), weight, Double::sum);
        }

        List<VideoPopularityEntry> top3 = scores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(3)
                .map(entry -> {
                    VideoPost v = videoRepository.findById(entry.getKey()).orElseThrow();
                    return new VideoPopularityEntry(null, v.getId(), v.getTitle(), entry.getValue());
                })
                .collect(Collectors.toList());


        PopularVideoReport report = new PopularVideoReport(top3);
        reportRepository.save(report);
    }
}
