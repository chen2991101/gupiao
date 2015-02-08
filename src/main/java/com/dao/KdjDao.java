package com.dao;

import com.entity.Kdj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KdjDao extends PagingAndSortingRepository<Kdj, String> {
    Page<Kdj> findByNo(String no, Pageable pageable);
}
