package com.service;

import com.entity.Kdj;
import com.entity.MACDRecords;
import com.entity.Market;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class KdjAble extends Thread {

    private MarketService service;
    private int size;
    private int page;
    List<Market> list;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal c;
    private BigDecimal rsv;
    private BigDecimal a100 = new BigDecimal("100");
    private BigDecimal a3 = new BigDecimal("3");
    private BigDecimal a2 = new BigDecimal("2");
    private BigDecimal a2_3 = new BigDecimal("2").divide(new BigDecimal("3"), 4, 4);

    public KdjAble(MarketService service, int size, int page) {
        this.size = size;
        this.service = service;
        this.page = page;
    }

    @Override
    public void run() {
        for (int j = page; j < page + size; j++) {
            list = service.findMarket(j);
            for (Market market : list) {
                List<MACDRecords> array = service.findByNoOrderByTime(market.getN());//获取所有的数据
                List<Kdj> kdjs = new ArrayList<Kdj>();
                for (int i = 0; i < array.size(); i++) {
                    MACDRecords records = array.get(i);
                    Kdj kdj = new Kdj();
                    kdj.setName(records.getName());
                    kdj.setNo(records.getNo());
                    kdj.setTime(records.getTime());
                    if (i == 0) {
                        //第一次获取
                        try {
                            c = records.getHighest().subtract(records.getLowest());
                            c = c.compareTo(BigDecimal.ZERO) == 0 ? a100 : c;
                            rsv = (records.getPrice().subtract(records.getLowest())).divide(c, 2, 4).
                                    multiply(a100);
                            kdj.setK(a100.multiply(a2_3).add(rsv.divide(a3, 2, 4)));
                            kdj.setD(a100.multiply(a2_3).add(kdj.getK().divide(a3, 2, 4)));
                            kdj.setJ(a3.multiply(kdj.getK()).subtract(a2.multiply(kdj.getD())));
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(records.getTime() + "_________" + records.getNo());
                        }
                    } else {
                        List<MACDRecords> l = service.findByNoAndTime(records.getNo(), records.getTime());
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
                        rsv = (records.getPrice().subtract(min)).divide(max.subtract(min), 4, 4).multiply(a100);
                        kdj.setK(kdjs.get(kdjs.size() - 1).getK().multiply(a2_3).add(rsv.divide(a3, 2, 4)));
                        kdj.setD(kdjs.get(kdjs.size() - 1).getD().multiply(a2_3).add(kdj.getK().divide(a3, 2, 4)));
                        kdj.setJ(a3.multiply(kdj.getK()).subtract(a2.multiply(kdj.getD())));
                    }
                    kdj.setK(kdj.getK().compareTo(a100) > 0 ? a100 : kdj.getK());
                    kdj.setD(kdj.getD().compareTo(a100) > 0 ? a100 : kdj.getD());
                    kdj.setJ(kdj.getJ().compareTo(a100) > 0 ? a100 : kdj.getJ());

                    kdj.setK(kdj.getK().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getK());
                    kdj.setD(kdj.getD().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getD());
                    kdj.setJ(kdj.getJ().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kdj.getJ());

                    kdjs.add(kdj);
                    service.saveKdj(kdj);
                }
            }
        }
        System.out.println("------------------线程执行完毕");
    }
}
