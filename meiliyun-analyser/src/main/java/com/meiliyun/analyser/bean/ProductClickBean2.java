package com.meiliyun.analyser.bean;

/**
 * 产品点击类
 */
public class ProductClickBean2 {

    private String timeRange;

    private String url;

    private String area;

    private String position;

    // 产品id
    private String pid;

    private String channel;

    private String ip;

    public ProductClickBean2() {
    }

    public ProductClickBean2(String timeRange, String url, String area, String position, String pid, String channel,
            String ip) {
        this.timeRange = timeRange;
        this.url = url;
        this.area = area;
        this.position = position;
        this.pid = pid;
        this.channel = channel;
        this.ip = ip;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "ProductClickBean2 [timeRange=" + timeRange + ", url=" + url + ", area=" + area + ", position="
                + position + ", pid=" + pid + ", channel=" + channel + ", ip=" + ip + "]";
    }

}
