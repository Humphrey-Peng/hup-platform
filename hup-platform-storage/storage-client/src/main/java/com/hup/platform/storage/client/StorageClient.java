package com.hup.platform.storage.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartRequest;

@FeignClient("platform-storage")
@RequestMapping("/storage")
public interface StorageClient {

    @PostMapping
    void upload(MultipartRequest request);
}
