package com;

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
}
