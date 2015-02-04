package com;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

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


    /**
     * 发送邮件
     *
     * @param msg
     */
    public static void sendEMail(String msg) {

        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        // 设定mail server
        senderImpl.setHost("smtp.163.com");

        // 建立邮件消息,发送简单邮件和html邮件的区别
        MimeMessage mailMessage = senderImpl.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, "gbk");

        // 设置收件人，寄件人
        try {
            messageHelper.setTo("195822080@qq.com");
            messageHelper.setFrom("chen2991101@163.com");
            messageHelper.setSubject("我的提示信息");
            // true 表示启动HTML格式的邮件

            messageHelper.setText(msg);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        senderImpl.setUsername("chen2991101@163.com"); // 根据自己的情况,设置username
        senderImpl.setPassword("195822080"); // 根据自己的情况, 设置password
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
        prop.put("mail.smtp.timeout", "25000");
        senderImpl.setJavaMailProperties(prop);
        // 发送邮件
        senderImpl.send(mailMessage);
    }


    /**
     * 获取今天有没有交易数据
     *
     * @return
     */
    public static String getGuPiaoDate() {
        String date = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet method = new HttpGet("http://qt.gtimg.cn/q=sz000001");
            HttpResponse response = httpClient.execute(method);
            String context = inputStream2String(response.getEntity().getContent());// 获取的信息
            String[] array = context.trim().split("~");
            date = array[30].substring(0, 8);
        } catch (Exception e) {
            e.printStackTrace();
            return getGuPiaoDate();
        }
        return date;
    }


/*        Records h = new Records();
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
        recordsDao.save(h);*/
}
