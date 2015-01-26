package com.dao;

import com.entity.Records;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordsDao extends PagingAndSortingRepository<Records, String> {

    @Query("from Records r where r.no=?1")
    Page<Records> findByNo(String no, Pageable pageable);

    // 放量三连阴
    @Query("select new Records(c.no,c.name,c.currentPrice) FROM Records a, Records b,Records c WHERE a.no=b.no and b.no=c.no and a.time =?1 AND b.time =?2 AND c.time = ?3 AND a.upanddown2<-1 AND b.upanddown2<-2 AND c.upanddown2<-2.5 AND a.ltsz<50 AND a.name NOT LIKE '%st%' AND a.currentPrice<20 AND a.deal<b.deal AND b.deal<c.deal")
    List<Records> moreDeal(int time1, int time2, int time3);

    // 缩量三连阴
    @Query("select new Records(c.no,c.name,c.currentPrice) FROM Records a, Records b,Records c WHERE a.no=b.no and b.no=c.no and a.time =?1 AND b.time =?2 AND c.time = ?3 AND a.upanddown2<-1 AND b.upanddown2<-2 AND c.upanddown2<-2.5 AND a.ltsz<50 AND a.name NOT LIKE '%st%' AND a.currentPrice<20 AND a.deal>b.deal AND b.deal>c.deal")
    List<Records> lessenDeal(int time1, int time2, int time3);

    @Query("SELECT r.time FROM Records r GROUP BY r.time")
    List<Integer> findTimes(Pageable pageable);

    Page<Records> findByTime(Integer time, Pageable pageable);


}
