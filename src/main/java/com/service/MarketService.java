package com.service;

import com.Utils;
import com.dao.*;
import com.entity.*;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    MacdCrossDao macdCrossDao;//macd金叉的dao层
    @Autowired
    TimeDao timeDao;
    @Autowired
    KdjDao kdjDao;
    @Autowired
    MACDDao macdDao;
    private int pageSize = 10;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private PageRequest pageRequest = new PageRequest(0, 1, new Sort(new Sort.Order(Sort.Direction.DESC, "time")));
    private PageRequest page9 = new PageRequest(0, 9, new Sort(new Sort.Order(Sort.Direction.DESC, "time")));

    BigDecimal a11_13 = new BigDecimal(11).divide(new BigDecimal(13), 4, 4);
    BigDecimal a2_10 = new BigDecimal(2).divide(new BigDecimal(10), 4, 4);
    BigDecimal a8_10 = new BigDecimal(8).divide(new BigDecimal(10), 4, 4);
    BigDecimal a2_13 = new BigDecimal(2).divide(new BigDecimal(13), 4, 4);
    BigDecimal a25_27 = new BigDecimal(25).divide(new BigDecimal(27), 4, 4);
    BigDecimal a2_27 = new BigDecimal(2).divide(new BigDecimal(27), 4, 4);
    BigDecimal two = new BigDecimal(2);

    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal c;
    private BigDecimal rsv;
    private BigDecimal a100 = new BigDecimal("100");
    private BigDecimal a3 = new BigDecimal("3");
    private BigDecimal a2 = new BigDecimal("2");
    private BigDecimal a2_3 = new BigDecimal("2").divide(new BigDecimal("3"), 4, 4);


    /**
     * 获取股票记录
     *
     * @param pageNo   页数
     * @param pageSize 每页的条数
     */
    public Map<String, Object> findRecords(int pageNo, int pageSize, final String q) {
        Map<String, Object> map = new HashedMap();
        final List<Integer> times = timeDao.findTime(new PageRequest(0, 1, new Sort(new Sort.Order(Sort.Direction.DESC, "time"))));

        Page<MACDRecords> page = macdRecordsDao.findAll(new Specification<MACDRecords>() {
            @Override
            public Predicate toPredicate(Root<MACDRecords> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(root.<Integer>get("time"), times.get(0)));
                if ((null != q) && !q.equals("")) {
                    //用户名
                    expressions.add(cb.or(cb.like(root.<String>get("no"), "%" + q + "%"), cb.like(root.<String>get("name"), "%" + q + "%")));
                }
                return predicate;
            }
        }, new PageRequest(pageNo - 1, pageSize, new Sort(new Sort.Order(Sort.Direction.DESC, "time"))));


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
        final String time = dateFormat.format(new Date());
        if (time.equals(Utils.getGuPiaoDate())) {
            // 如果进来有行情才获取数据
            //先添加时间
            List<Time> times = timeDao.findByTime(Integer.parseInt(time));
            if (times.size() == 0) {
                timeDao.save(new Time(Integer.parseInt(time)));
            }

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

            Utils.sendEMail("行情添加成功,正在添加kdj信息");

            //添加kdj数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addKdj(Integer.parseInt(time));
                }
            }).start();
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
    @Transactional
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
            httpClient(query);
        }
        return null;
    }

    /**
     * 添加记录
     *
     * @param str
     */
    public void addRecords(String str) {
        String[] array = str.split("~");
        BigDecimal deal = new BigDecimal(array[6]);//交易量
        if (deal.compareTo(BigDecimal.ZERO) != 0) {
            //当天没有停盘
            MACDRecords macdRecords = new MACDRecords();
            macdRecords.setNo(array[2]);
            macdRecords.setName(array[1]);
            macdRecords.setOpen(new BigDecimal(array[5]));//今开盘
            macdRecords.setHighest(new BigDecimal(array[33]));
            macdRecords.setLowest(new BigDecimal(array[34]));
            macdRecords.setPrice(new BigDecimal(array[3]));//当前价格
            macdRecords.setDeal(new BigDecimal(array[6]));//交易量
            macdRecords.setDealMoney(new BigDecimal(array[37]));//成交金额
            macdRecords.setTime(Integer.parseInt(array[30].substring(0, 8)));

            macdRecords.setOut_dish(Long.parseLong(array[7]));//外盘
            macdRecords.setIn_dish(Long.parseLong(array[8]));//内盘
            macdRecords.setUpanddown(new BigDecimal(array[31]));//涨幅
            macdRecords.setUpanddown2(Float.parseFloat(array[32]));//涨幅比
            macdRecordsDao.save(macdRecords);
            //保存成功，添加macd
            addMacd(macdRecords.getNo(), macdRecords.getName(), macdRecords.getTime(), macdRecords.getPrice());
        }
    }

    @Transactional
    public void addMacd(String no, String name, int time, BigDecimal price) {
        List<Macd> list = macdDao.findByNo(no, pageRequest).getContent();
        if (list.size() == 1) {
            Macd oldMacd = list.get(0);
            Macd macd = new Macd();
            macd.setTime(time);
            macd.setNo(no);
            macd.setName(name);
            macd.setEma12(oldMacd.getEma12().multiply(a11_13).add(price.multiply(a2_13)));
            macd.setEma26(oldMacd.getEma26().multiply(a25_27).add(price.multiply(a2_27)));
            macd.setDiff(macd.getEma12().subtract(macd.getEma26()).setScale(3, 4));
            macd.setDea(oldMacd.getDea().multiply(a8_10).add(macd.getDiff().multiply(a2_10)).setScale(3, 4));
            macd.setBar((macd.getDiff().subtract(macd.getDea())).multiply(two).setScale(3, 4));
            macdDao.save(macd);
        }
    }


    /**
     * 添加历史
     */
    public void addHistory() {
        long count = marketDao.count();

        int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
        int c = sumPage / 2;// 每页的条数
        new MyAble(this, c, 1).start();
        new MyAble(this, (int) (sumPage - c), 1 + c).start();
/*        new MyAble(this, c, 1).start();
        new MyAble(this, c, 1 + c).start();
        new MyAble(this, c, 1 + 2 * c).start();
        new MyAble(this, c, 1 + 3 * c).start();
        new MyAble(this, (int) (sumPage - 4 * c), 1 + 4 * c).start();*/
    }

    /**
     * 计算macd
     */
    public void macd() {
        long count = marketDao.count();
        int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
        int c = sumPage / 5;// 每页的条数
        new MACDAble(this, c, 1).start();
        new MACDAble(this, c, 1 + c).start();
        new MACDAble(this, c, 1 + 2 * c).start();
        new MACDAble(this, c, 1 + 3 * c).start();
        new MACDAble(this, (int) (sumPage - 4 * c), 1 + 4 * c).start();
    }

    public List<MACDRecords> findMacdRecordsByNo(String no) {
        return macdRecordsDao.findByNo(no, new Sort(new Sort.Order("time")));
    }

    public void addMACDRecords(List<Macd> list) {
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

    /**
     * 添加所有纪录的时间
     *
     * @return
     */
    @Transactional
    public boolean setAllTimes() {
        List<Integer> times = macdRecordsDao.findAllTime();//获取所有的时间
        for (Integer time : times) {
            Time t = new Time(time);
            timeDao.save(t);
        }
        return true;
    }

    /**
     * 获取macd金叉死叉的信息
     *
     * @return
     */
    @Transactional
    public List<MacdCross> findMacd() {
        List<MacdCross> macdCrosses = new ArrayList<MacdCross>();//需要返回的数据
        List<Time> times = timeDao.findAll(pageRequest).getContent();//获取最新的一个日期
        if (times != null && times.size() == 1) {
            Time time = times.get(0);//获取最新数据的时间
            switch (time.getMacdStatus()) {
                case 0:
                    //没有操作，需要初始化
                    time.setMacdStatus(1);//标示正在初始化
                    List<Integer> tt = timeDao.findTime(new PageRequest(0, 2, new Sort(new Sort.Order(Sort.Direction.DESC, "time"))));
                    List<Macd> macds = macdDao.findMacd(tt.get(1), tt.get(0));
                    for (Macd macd : macds) {
                        //转换数据
                        macdCrosses.add(new MacdCross(macd.getDiff(), macd.getNo(), macd.getName(), macd.getTime()));
                    }
                    macdCrossDao.save(macdCrosses);//保存数据
                    time.setMacdStatus(2);//加载完成
                    break;
                case 1:
                    //正在初始化当中
                    break;
                default:
                    macdCrosses = macdCrossDao.findByTimeOrderByDiffAsc(time.getTime());
                    //数据已经获取完毕，直接调用即可
            }
        }
        return macdCrosses;
    }

    /**
     * 添加kdj数据
     */
    public void addKdj(int time) {
/*        long count = marketDao.count();
        int sumPage = (int) (count / pageSize + (count % pageSize > 0 ? 1 : 0));// 总页数
        int c = sumPage / 5;// 每页的条数
        new KdjAble(this, c, 1).start();
        new KdjAble(this, c, 1 + c).start();
        new KdjAble(this, c, 1 + 2 * c).start();
        new KdjAble(this, c, 1 + 3 * c).start();
        new KdjAble(this, (int) (sumPage - 4 * c), 1 + 4 * c).start();*/


        List<Market> list = (List<Market>) marketDao.findAll();//获取所有的股票
        for (Market market : list) {
            MACDRecords records = macdRecordsDao.findByNoAndTime(market.getN(), time);
            if (records != null) {
                Kdj kdj = new Kdj();
                kdj.setNo(records.getNo());
                kdj.setName(records.getName());
                kdj.setTime(time);
                List<MACDRecords> l = findByNoAndTime(records.getNo(), records.getTime());
                for (int k = 0; k < l.size(); k++) {
                    MACDRecords m = l.get(k);
                    if (k == 0) {
                        max = m.getHighest();//最高价
                        min = m.getLowest();//最低价
                    } else {
                        max = max.compareTo(m.getHighest()) < 0 ? m.getHighest() : max;
                        min = min.compareTo(m.getLowest()) > 0 ? m.getLowest() : min;
                    }
                }


                BigDecimal c = max.subtract(min);
                if (c.compareTo(BigDecimal.ZERO) == 0) {
                    c = a100;
                }
                rsv = (records.getPrice().subtract(min)).divide(c, 4, 4).multiply(a100);

                List<Kdj> ks = kdjDao.findByNo(records.getNo(), pageRequest).getContent();
                if (ks != null && ks.size() == 1) {
                    Kdj kk = ks.get(0);
                    kdj.setK(kk.getK().multiply(a2_3).add(rsv.divide(a3, 2, 4)));
                    kdj.setD(kk.getD().multiply(a2_3).add(kdj.getK().divide(a3, 2, 4)));
                    kdj.setJ(a3.multiply(kdj.getK()).subtract(a2.multiply(kdj.getD())));

                    kdj.setK(kdj.getK().compareTo(a100) > 0 ? a100 : kdj.getK());
                    kdj.setD(kdj.getD().compareTo(a100) > 0 ? a100 : kdj.getD());
                    kdj.setJ(kdj.getJ().compareTo(a100) > 0 ? a100 : kdj.getJ());

                    kdj.setK(kdj.getK().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getK());
                    kdj.setD(kdj.getD().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getD());
                    kdj.setJ(kdj.getJ().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getJ());

                    kdjDao.save(kdj);
                }
            }
        }
        Utils.sendEMail("kdj行情添加成功");
    }

    @Transactional
    public void saveKdj(Kdj kdj) {
        kdjDao.save(kdj);
    }

    public List<MACDRecords> findByNoAndTime(String no, int time) {
        return macdRecordsDao.findByNoAndTime(no, time, page9).getContent();
    }


    public List<MACDRecords> findByNoOrderByTime(String no) {
        return macdRecordsDao.findByNoOrderByTimeAsc(no);
    }
}
