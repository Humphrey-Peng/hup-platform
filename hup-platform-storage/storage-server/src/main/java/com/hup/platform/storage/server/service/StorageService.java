package com.hup.platform.storage.server.service;

import org.springframework.web.multipart.MultipartRequest;

public interface StorageService {

    void upload(MultipartRequest request);

}
