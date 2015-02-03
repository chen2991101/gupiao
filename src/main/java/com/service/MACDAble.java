package com.service;

import com.entity.MACD;
import com.entity.MACDRecords;
import com.entity.Market;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MACDAble extends Thread {

    private MarketService service;
    private int size;
    private int page;
    List<Market> list;

    BigDecimal a11_13 = new BigDecimal(11).divide(new BigDecimal(13), 3, 4);
    BigDecimal a2_10 = new BigDecimal(2).divide(new BigDecimal(10), 3, 4);
    BigDecimal a8_10 = new BigDecimal(8).divide(new BigDecimal(10), 3, 4);
    BigDecimal a2_13 = new BigDecimal(2).divide(new BigDecimal(13), 3, 4);
    BigDecimal a25_27 = new BigDecimal(25).divide(new BigDecimal(27), 3, 4);
    BigDecimal a2_27 = new BigDecimal(2).divide(new BigDecimal(27), 3, 4);
    BigDecimal two = new BigDecimal(2);

    public MACDAble(MarketService service, int size, int page) {
        this.size = size;
        this.service = service;
        this.page = page;
    }

    @Override
    public void run() {
        MACD oldMacd = null;//前一天的macd
        for (int j = page; j < page + size; j++) {
            list = service.findMarket(j);
            for (Market market : list) {
                List<MACD> macdList = new ArrayList<MACD>();
                List<MACDRecords> list = service.findMacdRecordsByNo(market.getNo().substring(2));
                for (int i = 1; i < list.size(); i++) {
                    MACD macd = new MACD();
                    macd.setTime(list.get(i).getTime());
                    macd.setNo(list.get(i).getNo());
                    if (i == 1) {
                        macd.setEma12((list.get(1).getPrice().subtract(list.get(0).getPrice())).multiply(a2_13).add(list.get(0).getPrice()));
                        macd.setEma26((list.get(1).getPrice().subtract(list.get(0).getPrice())).multiply(a2_27).add(list.get(0).getPrice()));
                    } else {
                        oldMacd = macdList.get(macdList.size() - 1);
                        macd.setEma12(oldMacd.getEma12().multiply(a11_13).add(list.get(i).getPrice().multiply(a2_13)));
                        macd.setEma26(oldMacd.getEma26().multiply(a25_27).add(list.get(i).getPrice().multiply(a2_27)));
                    }
                    macd.setDiff(macd.getEma12().subtract(macd.getEma26()));
                    if (i == 1) {
                        macd.setDea(macd.getDiff().multiply(a2_10));
                    } else {
                        macd.setDea(oldMacd.getDea().multiply(a8_10).add(macd.getDiff().multiply(a2_10)));
                    }
                    macd.setBAR((macd.getDiff().subtract(macd.getDea())).multiply(two));
                    macdList.add(macd);
                }
                service.addMACDRecords(macdList);
            }
        }
        System.out.println("------------------线程执行完毕");
    }
}
