package com.dao;

import com.entity.KdjCross;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KdjCrossDao extends PagingAndSortingRepository<KdjCross, String> {

    List<KdjCross> findByTime(int time);//根据时间查询
}
