package com.hup.platform.mail.server.service.impl;


import com.hup.platform.mail.model.MailRequest;
import com.hup.platform.mail.server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String sender;


    @Override
    public void sendMail(MailRequest.MailContent mailContent) {

        Assert.notNull(mailContent, "请求参数为空！！");

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mailContent.getEmail());
            mimeMessageHelper.setSubject(mailContent.getSubject());
            mimeMessageHelper.setText(mailContent.getText());
            if (mailContent.getFile() != null) {
                FileSystemResource fileSystemResource = new FileSystemResource(mailContent.getFile());
                mimeMessageHelper.addAttachment(mailContent.getAttachmentName(), fileSystemResource);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);

    }
}
