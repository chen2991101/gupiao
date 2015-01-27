package com.service;

import com.Utils;
import com.dao.MarketDao;
import com.dao.RecordsDao;
import com.entity.Market;
import com.entity.Records;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作股票的service
 * Created by Administrator on 2015-01-21.
 */
@Service
public class MarketService {
    @Autowired
    MarketDao marketDao;
    @Autowired
    RecordsDao recordsDao;
    private int pageSize = 10;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");


    /**
     * 获取股票记录
     *
     * @param pageNo   页数
     * @param pageSize 每页的条数
     */
    public Map<String, Object> findRecords(int pageNo, int pageSize) {
        Map<String, Object> map = new HashedMap();
        List<Integer> times = recordsDao.findTimes(new PageRequest(0, 1, new Sort(new Sort.Order(Sort.Direction.DESC, "time"))));//获取最新的时间
        Page<Records> page = recordsDao.
                findByTime(times.get(0), new PageRequest(pageNo - 1, pageSize, new Sort(new Sort.Order(Sort.Direction.DESC, "time"))));
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
        return map;
    }

    /**
     * 添加股票信息
     *
     * @param isSh 是否是上海的股票
     */
    public void addMarket(boolean isSh) {
        String type;//股票的类型
        int marketNo;//股票代码
        int maxNo;//最大查询到的代码
        if (isSh) {
            type = "sh";
            marketNo = 600000;
            maxNo = 604000;
        } else {
            type = "sz";
            marketNo = 0;
            maxNo = 4000;
        }

        HttpClient httpClient = new DefaultHttpClient();//httpclient请求
        while (marketNo <= maxNo) {
            String query = "s_" + type + String.format("%06d", marketNo);//请求的查询条件

            if (marketNo + 19 < maxNo) {
                query += Utils.appendQuery(19, type, marketNo);
            } else {
                query += Utils.appendQuery(maxNo - marketNo - 1, type, marketNo);
            }

            HttpGet method = new HttpGet("http://qt.gtimg.cn/q=" + query);
            marketNo += 20;//设置新的数据
            try {
                HttpResponse response = httpClient.execute(method);
                saveMarket(Utils.inputStream2String(response.getEntity().getContent()));//保存股票信息
            } catch (Exception e) {
                e.printStackTrace();
            }
            method.releaseConnection();
        }
        Utils.sendEMail(type + "股票获取完毕");//发送邮件提醒
    }

    @Transactional
    private void saveMarket(String str) {
        String[] array = str.split(";");
        for (String st : array)
            if (st.indexOf("\"") != -1) {
                // 如果没有引号说明没有数据
                st = st.trim();
                marketDao.save(new Market(st.substring(4, st.indexOf("=")), st.substring(14, st.length() - 1).split("~")[1]));
            }
    }

    /**
     * 获取记录的总数量
     *
     * @return 总数量
     */
    public long findRecordsCount() {
        return recordsDao.count();
    }


    /**
     * 删除已经退市的股票
     */
    @Transactional
    public void deleteBack() {
        List<String> list = recordsDao.findBack();
        if (list != null) {
            int i = 0;
            for (String s : list) {
                i = marketDao.deleteBack(Integer.parseInt(s) > 500000 ? "sh" + s : "sz" + s);
                if (i > 0) {
                    recordsDao.deleteByNo(s);
                }
            }
        }
    }


    /**
     * 添加股票行情
     */
    public void addRecords() {
        if (dateFormat.format(new Date()).equals(Utils.getGuPiaoDate())) {
            // 如果进来有行情才获取数据
            long count = marketDao.count();//所有股票的数量
            int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
            List<Market> list;
            int index = 0;
            String query = "";
            for (int i = 0; i < sumPage; i++) {
                index = 0;
                list = marketDao.findAll(new PageRequest(i, pageSize)).getContent();//获取当前页的数据
                for (int j = 0; j < list.size(); j++) {
                    if (j == 0) {
                        query = list.get(j).getNo();
                    } else {
                        query += ("," + list.get(j).getNo());
                    }
                }
                HttpGet method = httpClient(query);
                if (method != null) {
                    method.releaseConnection();
                }
            }
            Utils.sendEMail("行情添加成功");
        } else {
            Utils.sendEMail("今天没有行情");
        }
    }


    /**
     * 获取数据
     *
     * @param query 查询
     * @return
     */
    private HttpGet httpClient(String query) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet method = new HttpGet("http://qt.gtimg.cn/q=" + query);
            HttpResponse response = httpClient.execute(method);
            String context = Utils.inputStream2String(response.getEntity()
                    .getContent());// 获取的信息
            save(context.trim());// 分割数据
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            httpClient(query);
        }
        return null;
    }

    private void save(String str) {
        String[] array = str.split(";");
        for (String st : array) {
            if (st.indexOf("\"") != -1) {
                // 如果没有引号说明没有数据
                addRecords(st.trim());// 如果是任务调度的话保存到任务调度中
            }
        }
    }


    /**
     * 添加记录
     *
     * @param str
     */
    @Transactional
    public void addRecords(String str) {
        String[] array = str.split("~");
        Records h = new Records();
        h.setName(array[1]);
        h.setNo(array[2]);
        h.setCurrentPrice(new BigDecimal(array[3]));
        h.setYesterday_income(new BigDecimal(array[4]));
        h.setToday_open(new BigDecimal(array[5]));
        h.setDeal(new BigDecimal(array[6]));
        h.setOut_dish(Integer.parseInt(array[7]));
        h.setIn_dish(Integer.parseInt(array[8]));
        h.setBuy1(new BigDecimal(array[9]));
        h.setBuy1l(Float.parseFloat(array[10]));
        h.setBuy2(new BigDecimal(array[11]));
        h.setBuy2l(Float.parseFloat(array[12]));
        h.setBuy3(new BigDecimal(array[13]));
        h.setBuy3l(Float.parseFloat(array[14]));
        h.setBuy4(new BigDecimal(array[15]));
        h.setBuy4l(Float.parseFloat(array[16]));
        h.setBuy5(new BigDecimal(array[17]));
        h.setBuy5l(Float.parseFloat(array[18]));
        h.setSale1(new BigDecimal(array[19]));
        h.setSale1l(Float.parseFloat(array[20]));
        h.setSale2(new BigDecimal(array[21]));
        h.setSale2l(Float.parseFloat(array[22]));
        h.setSale3(new BigDecimal(array[23]));
        h.setSale3l(Float.parseFloat(array[24]));
        h.setSale4(new BigDecimal(array[25]));
        h.setSale4l(Float.parseFloat(array[26]));
        h.setSale5(new BigDecimal(array[27]));
        h.setSale5l(Float.parseFloat(array[28]));
        h.setTime(Integer.parseInt(array[30].substring(0, 8)));
        h.setUpanddown(new BigDecimal(array[31]));
        h.setUpanddown2(Float.parseFloat(array[32]));
        h.setHeightest(new BigDecimal(array[33]));
        h.setLowest(new BigDecimal(array[34]));
        h.setDealAmount(new BigDecimal(array[37]));
        h.setHandover(Float.parseFloat(array[38].length() == 0 ? "0" : array[38]));
        h.setPe(Float.parseFloat(array[39].length() == 0 ? "0" : array[39]));
        h.setZf(Float.parseFloat(array[43]));
        h.setLtsz(array[44].length() == 0 ? BigDecimal.ZERO : new BigDecimal(array[44]));
        h.setTotalMoney(array[45].length() == 0 ? BigDecimal.ZERO : new BigDecimal(array[45]));
        h.setSjl(Float.parseFloat(array[46].length() == 0 ? "0" : array[46]));
        h.setZtj(new BigDecimal(array[47]));
        h.setDtj(new BigDecimal(array[48]));
        recordsDao.save(h);
    }


    /**
     * 查询股票的历史
     */
    public String findHistory() {
        HttpClient httpClient = new DefaultHttpClient();//httpclient请求
        String query = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/601000.phtml";
        HttpGet method = new HttpGet(query);
        String res = "";//返回的数据
        try {
            HttpResponse response = httpClient.execute(method);

            res = Utils.inputStream2String(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        method.releaseConnection();
        return res;
    }
}
