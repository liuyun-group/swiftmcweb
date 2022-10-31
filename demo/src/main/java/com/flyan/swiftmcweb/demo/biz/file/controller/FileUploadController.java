package com.flyan.swiftmcweb.demo.biz.file.controller;

import com.flyan.swiftmcweb.demo.biz.file.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统 - 文件服务
 *
 * @author flyan
 * @version 1.0
 * @date 10/31/22
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传接口
     *
     * @param file  上传文件
     * @return      file pretty key
     * @response    "file_pretty_key"
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }


}
