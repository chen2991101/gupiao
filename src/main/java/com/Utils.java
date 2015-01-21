package com;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 工具类方法
 * Created by Administrator on 2015-01-21.
 */
public class Utils {

    public static final String textutf8 = "text/html;charset=UTF-8";

    /**
     * 追加条件查询
     *
     * @param count    需要拼装的数量
     * @param type     股票的类型
     * @param marketNo 代码的起始位置
     * @return 拼装结果
     */
    public static String appendQuery(int count, String type, int marketNo) {
        String query = "";
        for (int i = 1; i <= count; i++) {
            query += ",s_" + type + String.format("%06d", (marketNo + i));
        }
        return query;
    }


    /**
     * 把请求回来的流转换成字符串
     *
     * @param is 请求获取的流
     * @return 转换的字符串
     */
    public static String inputStream2String(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "gbk"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while (null != (line = reader.readLine())) {
                sb.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
