package com.dao;

import com.entity.Market;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 股票信息的dao层
 */
@Repository
public interface MarketDao extends PagingAndSortingRepository<Market, String> {
}
