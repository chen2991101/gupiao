package com.dao;

import com.entity.Macd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MACDDao extends PagingAndSortingRepository<Macd, String> {

    Page<Macd> findByNo(String no, Pageable pageable);

    @Query("select new Macd(b.no,b.diff,b.time,m.name) FROM Macd a, Macd b,Market m WHERE m.n=a.no and a.no=b.no and a.time =?1 AND b.time =?2 and a.diff<a.dea  and b.diff>=b.dea order by b.diff")
    List<Macd> findMacd(int time1, int time2);

    @Query("select m.time from Macd m group by m.time order by m.time desc")
    List<Integer> findTime(Pageable pageable);
}
