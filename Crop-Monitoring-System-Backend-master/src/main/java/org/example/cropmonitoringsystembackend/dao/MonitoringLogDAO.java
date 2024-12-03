package org.example.cropmonitoringsystembackend.dao;

import org.example.cropmonitoringsystembackend.entity.impl.MonitoringLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringLogDAO extends JpaRepository<MonitoringLog, String> {
    //    List<MonitoringLog> findByMonitoringLogCodeOrMonitoringLogDate(String logCode, String logDate);
    @Query("SELECT m FROM MonitoringLog m WHERE m.log_code = :logCode OR m.log_date = :logDate")
    List<MonitoringLog> findByMonitoringLogCodeOrMonitoringLogDate(@Param("logCode") String logCode, @Param("logDate") String logDate);
}
