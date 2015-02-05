package com.dao;

import com.entity.Time;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeDao extends PagingAndSortingRepository<Time, String> {

    List<Time> findByTime(Integer time);


    @Query("select t.time from Time t")
    List<Integer> findTime(Pageable pageable);
    
}
