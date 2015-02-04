package com.service;

import com.Utils;
import com.dao.MACDDao;
import com.dao.MACDRecordsDao;
import com.dao.MarketDao;
import com.dao.RecordsDao;
import com.entity.MACD;
import com.entity.MACDRecords;
import com.entity.Market;
import com.entity.Records;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    @Autowired
    MACDRecordsDao macdRecordsDao;
    @Autowired
    MACDDao macdDao;
    private int pageSize = 10;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private PageRequest pageRequest = new PageRequest(0, 1, new Sort(new Sort.Order(Sort.Direction.DESC, "time")));

    private BigDecimal a11_13 = new BigDecimal(11).divide(new BigDecimal(13), 3, 4);
    private BigDecimal a2_10 = new BigDecimal(2).divide(new BigDecimal(10), 3, 4);
    private BigDecimal a8_10 = new BigDecimal(8).divide(new BigDecimal(10), 3, 4);
    private BigDecimal a2_13 = new BigDecimal(2).divide(new BigDecimal(13), 3, 4);
    private BigDecimal a25_27 = new BigDecimal(25).divide(new BigDecimal(27), 3, 4);
    private BigDecimal a2_27 = new BigDecimal(2).divide(new BigDecimal(27), 3, 4);
    private BigDecimal two = new BigDecimal(2);


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
            String query = "";
            for (int i = 0; i < sumPage; i++) {
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

            String[] array = context.trim().split(";");
            for (String st : array) {
                if (st.indexOf("\"") != -1) {
                    // 如果没有引号说明没有数据
                    addRecords(st.trim());// 如果是任务调度的话保存到任务调度中
                }
            }
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            httpClient(query);
        }
        return null;
    }

    /**
     * 添加记录
     *
     * @param str
     */
    @Transactional
    public void addRecords(String str) {
        String[] array = str.split("~");
        float upanddown2 = Float.parseFloat(array[32]);//股票当天的涨幅
        BigDecimal deal = new BigDecimal(array[6]);//交易量
        if (upanddown2 != 0 && deal.compareTo(BigDecimal.ZERO) != 0) {
            //当天没有停盘
            MACDRecords macdRecords = new MACDRecords();
            macdRecords.setNo(array[2]);
            macdRecords.setOpen(new BigDecimal(array[5]));//今开盘
            macdRecords.setHighest(new BigDecimal(array[33]));
            macdRecords.setLowest(new BigDecimal(array[34]));
            macdRecords.setPrice(new BigDecimal(array[3]));//当前价格
            macdRecords.setDeal(new BigDecimal(array[6]));//交易量
            macdRecords.setDealMoney(new BigDecimal(array[37]));//成交金额
            macdRecords.setTime(Integer.parseInt(array[30].substring(0, 8)));
            macdRecordsDao.save(macdRecords);
            //保存成功，添加macd
            addMacd(macdRecords.getNo(), macdRecords.getTime(), macdRecords.getPrice());
        }
    }

    @Transactional
    public void addMacd(String no, int time, BigDecimal price) {
        List<MACD> list = macdDao.findByNo(no, pageRequest).getContent();
        if (list.size() == 1) {
            MACD oldMacd = list.get(0);
            MACD macd = new MACD();
            macd.setTime(time);
            macd.setNo(no);
            macd.setEma12(oldMacd.getEma12().multiply(a11_13).add(price.multiply(a2_13)));
            macd.setEma26(oldMacd.getEma26().multiply(a25_27).add(price.multiply(a2_27)));
            macd.setDiff(macd.getEma12().subtract(macd.getEma26()));
            macd.setDea(oldMacd.getDea().multiply(a8_10).add(macd.getDiff().multiply(a2_10)));
            macd.setBar((macd.getDiff().subtract(macd.getDea())).multiply(two));
            macdDao.save(macd);
        }
    }


    /**
     * 添加历史
     */
    public void addHistory() {
        long count = marketDao.count();
        int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
        int c = sumPage / 4;// 每页的条数
        new MyAble(this, c, 1).start();
        new MyAble(this, c, 1 + c).start();
        new MyAble(this, c, 1 + 2 * c).start();
        new MyAble(this, c, 1 + 3 * c).start();
        new MyAble(this, (int) (sumPage - 4 * c), 1 + 4 * c).start();
    }

    /**
     * 计算macd
     */
    public void macd() {
        long count = marketDao.count();
        int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
        int c = sumPage / 4;// 每页的条数
        new MACDAble(this, c, 1).start();
        new MACDAble(this, c, 1 + c).start();
        new MACDAble(this, c, 1 + 2 * c).start();
        new MACDAble(this, c, 1 + 3 * c).start();
        new MACDAble(this, (int) (sumPage - 4 * c), 1 + 4 * c).start();
    }

    public List<MACDRecords> findMacdRecordsByNo(String no) {
        return macdRecordsDao.findByNo(no, new Sort(new Sort.Order("time")));
    }

    public void addMACDRecords(List<MACD> list) {
        if (list.size() > 0) {
            macdDao.save(list);
        }
    }

    @Transactional
    public void addRecord(String no, int j) {
        String url = "";
        Document doc = null;
        if (j == 0) {
            url = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + no + ".phtml";
        } else {
            url = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/" + no + ".phtml?year=2014&jidu=4";
        }

        try {
            doc = Jsoup.connect(url).timeout(9000).get();
        } catch (IOException e) {
            addRecord(no, j);
        }
        if (doc != null) {
            try {
                Element element = doc.getElementById("FundHoldSharesTable");
                Elements elements = null;

                elements = element.getElementsByTag("tr");


                if (element != null && elements.size() > 2) {
                    int time;//股票的时间
                    for (int i = 2; i < elements.size(); i++) {
                        String[] array = elements.get(i).text().split(" ");
                        time = Integer.parseInt(array[0].replaceAll("-", ""));
                        MACDRecords records = new MACDRecords();
                        records.setNo(no);
                        records.setOpen(new BigDecimal(array[1]));
                        records.setHighest(new BigDecimal(array[2]));
                        records.setPrice(new BigDecimal(array[3]));
                        records.setLowest(new BigDecimal(array[4]));
                        records.setDeal(new BigDecimal(array[5]));
                        records.setDealMoney(new BigDecimal(array[6]));
                        records.setTime(time);
                        macdRecordsDao.save(records);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 查询股票
     */
    @Transactional
    public List<Market> findMarket(int page) {
        Page<Market> p = marketDao.findAll(new PageRequest(page - 1, pageSize));
        return p.getContent();
    }
}
