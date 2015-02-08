package com.dao;

import com.entity.Kdj;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KdjDao extends PagingAndSortingRepository<Kdj, String> {
}
