package com.flyan.swiftmcweb.demo.biz.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 *
 * @author flyan
 * @version 1.0
 * @date 10/31/22
 */
public interface FileUploadService {


    /**
     * 文件上传是特殊接口，故不使用消息通信
     *
     * @param uploadFile    上传文件
     * @return
     */
    String uploadFile(MultipartFile uploadFile);

}
