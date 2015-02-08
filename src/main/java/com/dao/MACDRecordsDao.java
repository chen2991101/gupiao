package com.dao;

import com.entity.MACDRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MACDRecordsDao extends PagingAndSortingRepository<MACDRecords, String> {

    List<MACDRecords> findByNo(String no, Sort sort);

    Page<MACDRecords> findAll(Specification<MACDRecords> personName, Pageable pageable);

    List<MACDRecords> findByNoOrderByTimeAsc(String no);

    @Query("from MACDRecords m where m.no=?1 and m.time<=?2")
    Page<MACDRecords> findByNoAndTime(String no, int time, Pageable pageable);

    MACDRecords findByNoAndTime(String no, int time);
}
