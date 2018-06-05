package com.hup.platform.mail.controller;


import com.hup.platform.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping
    public void sendMail(@RequestBody MailRequest.MailContent mailContent) {
        mailService.sendMail(mailContent);
    }
}
