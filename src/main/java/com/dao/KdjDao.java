package com.dao;

import com.entity.Kdj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KdjDao extends PagingAndSortingRepository<Kdj, String> {
    Page<Kdj> findByNo(String no, Pageable pageable);


    @Query("select new Kdj(b.no,b.name,b.time,b.k,b.d,b.j) FROM Kdj a,Kdj b where a.no=b.no and a.time=?1 and b.time=?2 AND a.k<b.k and a.d<b.d and a.j<b.j AND a.j<a.k AND b.j>b.k and a.k<a.d and b.k >b.d")
    List<Kdj> findCross(int time1, int time2);
}
