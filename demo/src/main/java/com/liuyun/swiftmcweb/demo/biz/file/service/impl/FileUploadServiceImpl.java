package com.liuyun.swiftmcweb.demo.biz.file.service.impl;

import cn.hutool.core.util.IdUtil;
import com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil;
import com.liuyun.swiftmcweb.core.util.time.TimeUtils;
import com.liuyun.swiftmcweb.demo.biz.file.service.FileUploadService;
import com.liuyun.swiftmcweb.demo.biz.common.enums.ErrorCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.liuyun.swiftmcweb.core.util.exception.ServiceExceptionUtil.exception;
import static java.io.File.separator;

/**
 * @author flyan
 * @version 1.0
 * @date 10/31/22
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private static final String BASE_FILE_PATH = System.getProperty("user.dir");
    private static final String FILE_PATH_PREFIX = "static/file";
    private static final String FILE_DATE_DIR_FORMAT = "yyyy-MM-dd";

    @Override
    public String uploadFile(MultipartFile uploadFile) {
        if(uploadFile.isEmpty()) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERR_FILE_NONE);
        }

        var now = TimeUtils.now();
        var fileDateDir = TimeUtils.dateToString(now, FILE_DATE_DIR_FORMAT);
        var fileDir = new File(BASE_FILE_PATH, FILE_PATH_PREFIX + separator + fileDateDir);
        if(!fileDir.exists() && !fileDir.mkdirs()) {
            log.error("创建文件目录失败：" + fileDir.getPath());
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERR_DIR_CREAT_FAILED, fileDir.getParentFile());
        }

        var originFilename = uploadFile.getOriginalFilename();
        log.info("-----------文件原始的名字【" + originFilename + "】-----------");
        assert originFilename != null;
        var saveFilename = System.currentTimeMillis() + IdUtil.fastSimpleUUID() + originFilename.substring(originFilename.lastIndexOf("."));
        log.info("-----------文件要保存后的新名字【" + saveFilename + "】-----------");

        var saveFile = new File(fileDir, saveFilename);
        assert !saveFile.exists();
        try {
            uploadFile.transferTo(saveFile);
            log.info("文件存储成功：" + saveFile.getPath());
            return FILE_PATH_PREFIX + separator + fileDateDir + separator + saveFilename;
        } catch (IOException e) {
            log.error("存储上传文件失败，原因：" + e.getMessage());
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ERR_SAVE_UPLOAD_FILE_FAILED, e.getMessage());
        }
    }

}
