package com.hup.platform.mail.service;

import com.hup.platform.mail.controller.MailRequest;

public interface MailService {

    void sendMail(MailRequest.MailContent mailContent);
}
