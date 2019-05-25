package org.air.bigearth.apps.file.exception;

import org.apache.tomcat.util.http.fileupload.FileUploadException;


/**
 * 自定义文件名超长异常类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-11
 */
public class FileNameLengthLimitExceededException extends FileUploadException
{
    private static final long serialVersionUID = 1L;
    private int length;
    private int maxLength;
    private String filename;

    public FileNameLengthLimitExceededException(String filename, int length, int maxLength)
    {
        super("file name : [" + filename + "], length : [" + length + "], max length : [" + maxLength + "]");
        this.length = length;
        this.maxLength = maxLength;
        this.filename = filename;
    }

    public String getFilename()
    {
        return filename;
    }

    public int getLength()
    {
        return length;
    }

    public int getMaxLength()
    {
        return maxLength;
    }
}
