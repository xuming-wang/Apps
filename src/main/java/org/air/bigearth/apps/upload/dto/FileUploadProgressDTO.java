package org.air.bigearth.apps.upload.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 创建进度实体类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-10
 */
public class FileUploadProgressDTO {
    /**
     * 已读字节
     */
    private Long readBytes = 0L;
    /**
     * 已读MB
     */
    private Double readMB = 0.00;
    /**
     * 总长度
     */
    private Long fileSize = 0L;
    /**
     * 目前正在读取第几个文件
     */
    private int itemIndex = 0;
    /**
     * 已读百分比
     */
    private String readPercent = "0%";
    /**
     * 读取速度
     */
    private Double speed = 0.00;
    /**
     * 开始读取的时间
     */
    private long startReadTime = System.currentTimeMillis();

    public Long getReadBytes() {
        return readBytes;
    }

    public void setReadBytes(Long readBytes) {
        this.readBytes = readBytes;
    }

    public Double getReadMB() {
        readMB = this.divideNumber(this.readBytes, 1024*1024);
        return readMB;
    }

    public void setReadMB(Double readMB) {
        this.readMB = readMB;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getReadPercent() {
        readPercent = this.getPercent(this.readBytes, this.fileSize);
        return readPercent;
    }

    public void setReadPercent(String readPercent) {
        this.readPercent = readPercent;
    }

    public Double getSpeed() {
        speed = this.divideNumber(this.divideNumber(this.readBytes * 1000, System.currentTimeMillis() - this.startReadTime), 1024 * 1024);
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public long getStartReadTime() {
        return startReadTime;
    }

    public void setStartReadTime(long startReadTime) {
        this.startReadTime = startReadTime;
    }

    /**
     * 转换为BigDecimal
     *
     * @param o
     */
    private BigDecimal toBig(Object o) {
        if (o == null || o.toString().equals("") || o.toString().equals("NaN")) {
            return new BigDecimal(0);
        }
        return new BigDecimal(o.toString());
    }

    @Override
    public String toString(){
        return "{\"readBytes\":" + this.readBytes + ",\"readMB\":" + this.readMB + ",\"fileSize\":" + this.fileSize+",\"itemIndex\":"+this.itemIndex+",\"readPercent\":"+this.readPercent+",\"speed\":"+this.speed+",\"startReadTime\":\""+this.startReadTime+"\"}";
    }

    /**
     * 计算比例
     *
     * @param divisor
     * @param dividend
     * @return double
     */
    private double divideNumber(Object divisor, Object dividend){
        if(divisor == null || dividend == null){
            return 0.00;
        }
        BigDecimal a = toBig(divisor);
        BigDecimal b = toBig(dividend);
        if(a.equals(toBig(0)) || b.equals(toBig(0))){
            return 0.00;
        }
        BigDecimal c = a.divide(b, 2, BigDecimal.ROUND_DOWN);
        return c.doubleValue();
    }

    /**
     * 计算百分比
     *
     * @param divisor
     * @param dividend
     * @return double
     */
    public String getPercent(Object divisor, Object dividend){
        if(divisor == null || dividend == null){
            return "0%";
        }
        NumberFormat percent = NumberFormat.getPercentInstance();
        //建立百分比格式化引用
        percent.setMaximumFractionDigits(2);
        BigDecimal a = toBig(divisor);
        BigDecimal b = toBig(dividend);
        if(a.equals(toBig(0)) || b.equals(toBig(0)) || a.equals(toBig(0.0)) || b.equals(toBig(0.0))){
            return "0%";
        }
        BigDecimal c = a.divide(b, 4, BigDecimal.ROUND_DOWN);
        return percent.format(c);
    }

}

