package com.service;

import com.dao.AddressDao;
import com.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 位置服务的service
 * Created by hao on 2015/1/24.
 */
@Service
public class AddressService {
    @Autowired
    AddressDao addressDao;

    /**
     * 上传位置信息
     */
    @Transactional
    public void uploadAddress(Address address) {
        addressDao.save(address);
    }

}
