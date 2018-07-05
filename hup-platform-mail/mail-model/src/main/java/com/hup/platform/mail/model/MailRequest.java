package com.hup.platform.mail.model;

import lombok.Data;

import java.io.File;

public interface MailRequest {

    @Data
    class MailContent {
        private File file;

        private String email;

        private String subject;

        private String text;

        private String attachmentName;
    }
}
