package com.dao;

import com.entity.Market;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 股票信息的dao层
 */
@Repository
public interface MarketDao extends PagingAndSortingRepository<Market, String> {

    @Modifying
    @Query("delete from Market m where m.no=?1")
    int deleteBack(String no);
}
