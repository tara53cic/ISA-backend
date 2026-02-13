package isa.jutjubic.repository;

import isa.jutjubic.model.PopularVideoReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularVideoReportRepository extends JpaRepository<PopularVideoReport, Long> {

    PopularVideoReport findFirstByOrderByExecutionTimeDesc();
}
