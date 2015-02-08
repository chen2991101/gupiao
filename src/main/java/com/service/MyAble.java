package com.service;

import com.entity.Market;

import java.util.List;

public class MyAble extends Thread {

    private MarketService service;
    private int size;
    private int page;
    List<Market> list;

    public MyAble(MarketService service, int size, int page) {
        this.size = size;
        this.service = service;
        this.page = page;
    }

    @Override
    public void run() {
        for (int i = page; i < page + size; i++) {
            list = service.findMarket(i);
            for (Market market : list) {
                service.addRecord(market.getNo().substring(2), 1);
//                for (int j = 0; j < 2; j++) {
//                }
            }
        }
        System.out.println("------------------线程执行完毕");
    }
}
