package com.service;

import com.dao.AddressDao;
import com.entity.Address;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    /**
     * 查询地址记录
     *
     * @param pageNo 页数
     * @param rows   每页的条数
     */
    public Map<String, Object> find(int pageNo, int rows) {
        Page<Address> page = addressDao.findAll(new PageRequest(pageNo - 1, rows, new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"))));
        Map<String, Object> map = new HashedMap();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
        return map;
    }
}
