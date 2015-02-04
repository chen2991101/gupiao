package com.dao;

import com.entity.MACD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MACDDao extends PagingAndSortingRepository<MACD, String> {

    Page<MACD> findByNo(String no, Pageable pageable);
}
