package com.dao;

import com.entity.IpAddress;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;

@Repository
public interface IpAddressDao extends PagingAndSortingRepository<IpAddress, String> {
}
