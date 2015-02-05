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

    @Query("FROM Macd r WHERE r.time =?1  and r.diff<r.dea")
    List<Macd> findDiffLtDea(int time1);

    @Query("FROM Macd r WHERE r.time =?1  and r.diff>r.dea")
    List<Macd> findDiffBtDea(int time1);

    @Query("select m.time from Macd m group by m.time order by m.time desc")
    List<Integer> findTime(Pageable pageable);
}
