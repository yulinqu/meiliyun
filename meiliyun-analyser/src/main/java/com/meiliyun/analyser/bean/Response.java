package com.meiliyun.analyser.bean;

import java.math.BigDecimal;

/**
 * 返回体
 * 
 * @author yulinqu
 */
public class Response {

    private String dateGap;

    private String url;

    private String advertisment;

    private String mainPosition;

    private Integer subPosition;

    private BigDecimal clickNum;

    public Response() {
    }

    public Response(String dateGap, String url, String advertisment, String mainPosition, Integer subPosition,
            BigDecimal clickNum) {
        this.dateGap = dateGap;
        this.url = url;
        this.advertisment = advertisment;
        this.mainPosition = mainPosition;
        this.subPosition = subPosition;
        this.clickNum = clickNum;
    }

    public String getDateGap() {
        return dateGap;
    }

    public void setDateGap(String dateGap) {
        this.dateGap = dateGap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdvertisment() {
        return advertisment;
    }

    public void setAdvertisment(String advertisment) {
        this.advertisment = advertisment;
    }

    public String getMainPosition() {
        return mainPosition;
    }

    public void setMainPosition(String mainPosition) {
        this.mainPosition = mainPosition;
    }

    public Integer getSubPosition() {
        return subPosition;
    }

    public void setSubPosition(Integer subPosition) {
        this.subPosition = subPosition;
    }

    public BigDecimal getClickNum() {
        return clickNum;
    }

    public void setClickNum(BigDecimal clickNum) {
        this.clickNum = clickNum;
    }

}
