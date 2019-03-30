package com.mxwbq.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by mxwbq on 2019/3/29.
 */
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    // 目标邮箱地址
    private String toEmail = "123456789@163.com";

    @Autowired
    private TemplateEngine templateEngine;

    // 简单邮件
    @RequestMapping("sendSimpleEmail")
    public String sendSimpleEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toEmail); // 接收地址
            message.setSubject("新年好"); // 标题
            message.setText("新年快乐，完事大吉\n 新年新气象"); // 内容
            javaMailSender.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 发送Html格式邮件
    @RequestMapping("sendHtmlEmail")
    public String sendHtmlEmail() {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail); // 接收地址
            helper.setSubject("HTML格式的邮件"); // 标题
            // 带HTML格式的内容
            StringBuffer sb = new StringBuffer(
                    "<h1><p style='color:#42b983'>使用Spring Boot发送HTML格式邮件</p></h1>" +
                    "<p>这里是正文:</p>" +
                    "<p>这里是听雨轩地址:www.nucode.cn</p>");
            helper.setText(sb.toString(), true);
            javaMailSender.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 发送邮件携带附件
    @RequestMapping("sendAttachmentsMail")
    public String sendAttachmentsMail() {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail); // 接收地址
            helper.setSubject("带附件的邮件"); // 标题
            helper.setText("详情参见附件内容！"); // 内容
            // 传入附件
            FileSystemResource file = new FileSystemResource(new File("src\\main\\resources\\static\\file\\SpringBoot_Email文档.docx"));
            helper.addAttachment("SpringBoot_Email文档.docx", file);
            javaMailSender.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 发送附带静态资源的邮件
    @RequestMapping("sendInlineMail")
    public String sendInlineMail() {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail); // 接收地址
            helper.setSubject("带静态资源的邮件"); // 标题
            helper.setText("<html><body>生日快乐：<img src='cid:img'/></body></html>", true); // 内容
            // 传入附件
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/img/礼物.jpg"));
            helper.addInline("img", file);
            javaMailSender.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // 发送模板邮件
    @RequestMapping("sendTemplateEmail")
    public String sendTemplateEmail(String registerURL) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail); // 接收地址
            helper.setSubject("邮件模板测试"); // 标题
            // 处理邮件模板
            Context context = new Context();
            context.setVariable("registerURL", registerURL);
            String template = templateEngine.process("register", context);
            helper.setText(template, true);
            javaMailSender.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
