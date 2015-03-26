package com.dao;

import com.entity.MacdCross;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MacdCrossDao extends PagingAndSortingRepository<MacdCross, String> {

    List<MacdCross> findByTimeOrderByDiffAsc(int time);//根据时间查询
}
