package com.dao;

import com.entity.Address;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * 地址信息的dao层
 */
@Repository
public interface AddressDao extends PagingAndSortingRepository<Address, String> {
}
