package org.air.bigearth.apps.file.domain;

/**
 * 图像元数据
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class ImageMetadata {

    private String datakey;

    private String uploadSource;

    private String createTime;

    public String getDatakey() {
        return datakey;
    }

    public void setDatakey(String datakey) {
        this.datakey = datakey;
    }

    public String getUploadSource() {
        return uploadSource;
    }

    public void setUploadSource(String uploadSource) {
        this.uploadSource = uploadSource;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
