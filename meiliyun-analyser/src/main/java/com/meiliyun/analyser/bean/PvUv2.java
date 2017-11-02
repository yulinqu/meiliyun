package com.meiliyun.analyser.bean;

public class PvUv2 {

    private String timeRange;

    private String url;

    private String uuid;

    private String channel;

    private String ip;

    public PvUv2() {
    }

    public PvUv2(String timeRange, String url, String uuid, String channel, String ip) {
        this.timeRange = timeRange;
        this.url = url;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
        return "PvUv2 [timeRange=" + timeRange + ", url=" + url + ", uuid=" + uuid + ", channel=" + channel + ", ip="
                + ip + "]";
    }

}
