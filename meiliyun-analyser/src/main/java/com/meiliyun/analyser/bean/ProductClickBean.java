package com.meiliyun.analyser.bean;

/**
 * 产品点击类
 */
public class ProductClickBean {

    private String timeRange;

    private String referUrl;

    private String area;

    private String position;

    private String pid;

    private String channel;

    private int clickCount;

    public ProductClickBean() {
    }

    public ProductClickBean(String timeRange, String referUrl, String area, String position, String pid, String channel,
            int clickCount) {
        this.timeRange = timeRange;
        this.referUrl = referUrl;
        this.area = area;
        this.position = position;
        this.pid = pid;
        this.channel = channel;
        this.clickCount = clickCount;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "ProductClickBean [timeRange=" + timeRange + ", referUrl=" + referUrl + ", area=" + area + ", position="
                + position + ", pid=" + pid + ", clickCount=" + clickCount + "]";
    }

}
