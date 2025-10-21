package com.example.actuator.metric.gauge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QueueStatusRepository extends JpaRepository<QueueStatus, Long> {

    @Query("""
      select new com.example.actuator.metric.gauge.ServerCnt(q.serverNo, count(q))
      from QueueStatus q
      where q.statusTime = (
        select max(q2.statusTime)
        from QueueStatus q2
        where q2.serverNo = q.serverNo
      )
      group by q.serverNo
      order by q.serverNo
    """)
    List<ServerCnt> findByServerInfo();
}
