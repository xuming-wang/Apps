package org.air.bigearth.apps.upload.listener;

import org.air.bigearth.apps.upload.dto.FileUploadProgressDTO;
import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * 监控文件上传进度
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-24
 */
@Component
public class FileUploadProgressListener implements ProgressListener {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadProgressListener.class);
    private final String FILE_UPLOAD_PROGRESS = "FileUploadProgress";
    private HttpSession session;

    public void setSession(HttpSession session){
        this.session = session;
        FileUploadProgressDTO progressDTO = new FileUploadProgressDTO();
        session.setAttribute(FILE_UPLOAD_PROGRESS, progressDTO);
    }

    /**
     * @param readBytes 到目前为止读取文件的比特数
     * @param fileSize 文件总大小
     * @param itemIndex 目前正在读取第几个文件
     */
    @Override
    public void update(long readBytes, long fileSize, int itemIndex) {
        FileUploadProgressDTO progressDTO = (FileUploadProgressDTO) session.getAttribute(FILE_UPLOAD_PROGRESS);
        progressDTO.setReadBytes(readBytes);
        progressDTO.setFileSize(fileSize);
        progressDTO.setItemIndex(itemIndex);
        session.setAttribute(FILE_UPLOAD_PROGRESS, progressDTO);
    }
}

