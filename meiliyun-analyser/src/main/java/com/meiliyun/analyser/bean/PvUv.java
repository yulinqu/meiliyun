package com.meiliyun.analyser.bean;

public class PvUv {

    private String timeRange;

    private String url;

    private int pv;

    private int uv;

    private String uuid;

    public PvUv() {
    }

    public PvUv(String timeRange, String url,String uuid) {
        this.timeRange = timeRange;
        this.url = url;
        this.uuid=uuid;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "PvUv [timeRange=" + timeRange + ", url=" + url + ", pv=" + pv + ", uv=" + uv + "]";
    }

}
