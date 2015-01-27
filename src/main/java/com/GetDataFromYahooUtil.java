package com;

import com.entity.Records;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015-01-27.
 */
public class GetDataFromYahooUtil {
    public static final String YAHOO_FINANCE_URL = "http://table.finance.yahoo.com/table.csv?";
    public static final String YAHOO_FINANCE_URL_TODAY = "http://download.finance.yahoo.com/d/quotes.csv?";

    /**
     * 根据 股票编码、开始日期、结束日期 获取股票数据
     *
     * @param stockName 沪市：000000.ss 深市：000000.sz
     * @param fromDate  开始日期
     * @param toDate    结束日期
     * @return List<StockData>
     * @author 祁丛生
     */
    public static List<Records> getStockCsvData(String stockName, String fromDate, String toDate) {
        List<Records> list = new ArrayList<Records>();
        String[] datefromInfo = fromDate.split("-");
        String[] toDateInfo = toDate.split("-");
        String code = stockName.substring(0, 6);

        String a = (Integer.valueOf(datefromInfo[1]) - 1) + "";// a – 起始时间，月
        String b = datefromInfo[2];// b – 起始时间，日
        String c = datefromInfo[0];// c – 起始时间，年
        String d = "0";// d – 结束时间，月
        String e = "25";// e – 结束时间，日
        String f = "2015";// f – 结束时间，年

        String params = "&a=" + a + "&b=" + b + "&c=" + c + "&d=" + d + "&e="
                + e + "&f=" + f;
        String url = YAHOO_FINANCE_URL + "s=" + stockName + params;

        InputStreamReader ins = null;
        BufferedReader in = null;

        try {

            HttpClient httpClient = new DefaultHttpClient();//httpclient请求

            HttpGet method = new HttpGet(url);
            HttpResponse response = httpClient.execute(method);
            method.releaseConnection();

            ins = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
            in = new BufferedReader(ins);

            String newLine = in.readLine();// 标题行

            while ((newLine = in.readLine()) != null) {
                String stockInfo[] = newLine.trim().split(",");
                Records sd = new Records();
                sd.setNo(code);
                sd.setTime(Integer.parseInt(stockInfo[0].replaceAll("-", "")));//时间

                sd.setToday_open(new BigDecimal(stockInfo[1]).setScale(2, 4));
                sd.setHeightest(new BigDecimal(stockInfo[2]).setScale(2, 4));
                sd.setLowest(new BigDecimal(stockInfo[3]).setScale(2, 4));
                sd.setCurrentPrice(new BigDecimal(stockInfo[4]));
                sd.setDeal(new BigDecimal(stockInfo[5]));
                list.add(sd);
            }

        } catch (Exception ex) {
            return null; //无交易数据
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
        return list;
    }

    /**
     * 根据 股票编码、日期 获取股票数据
     *
     * @param stockName 沪市：000000.ss 深市：000000.sz
     * @param date      日期
     * @return StockData
     * @author 祁丛生
     */
    public static List<Records> getStockCsvData(String stockName, String date) {
        return getStockCsvData(stockName, date, date);
    }

    /**
     * 根据 股票编码 获取当天股票数据
     *
     * @param stockName 沪市：000000.ss 深市：000000.sz
     * @return StockData
     * @author 祁丛生
     */
    public static Records getStockCsvData(String stockName) {
        String date = String.format("%1$tF", new Date());
        List<Records> list = getStockCsvData(stockName, date, date);
        return ((list != null && list.size() > 0) ? list.get(0) : null);
    }
}
