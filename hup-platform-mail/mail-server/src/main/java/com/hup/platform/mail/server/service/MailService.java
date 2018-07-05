package com.hup.platform.mail.server.service;


import com.hup.platform.mail.model.MailRequest;

public interface MailService {

    void sendMail(MailRequest.MailContent mailContent);
}
