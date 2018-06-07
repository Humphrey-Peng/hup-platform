package com.hup.platform.storage.server.service.impl;

import com.hup.platform.storage.server.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    @Override
    public void upload(MultipartRequest request) {
        String toPath = "/var/www/html/file/storage";
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");

        if (CollectionUtils.isEmpty(files)) {
            log.info("上传文件为空！！");
            return;
        }
        File file = new File(toPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (MultipartFile multipartFile : files) {
            String filename = multipartFile.getOriginalFilename();
            String suffix = filename.substring(filename.lastIndexOf("."));
            String uuid = UUID.randomUUID() + suffix;
            File serverFile = new File(toPath + uuid);
            try {
                multipartFile.transferTo(serverFile);
            } catch (IOException e) {
                log.info("io 异常！！");
                e.printStackTrace();
            }
            log.info("上传文件成功！！");
        }
    }
}
