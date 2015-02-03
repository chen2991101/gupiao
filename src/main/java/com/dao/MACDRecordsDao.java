package com.dao;

import com.entity.MACDRecords;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MACDRecordsDao extends PagingAndSortingRepository<MACDRecords, String> {

    List<MACDRecords> findByNo(String no,Sort sort);
}
