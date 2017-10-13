package com.meiliyun.analyser.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 初始化配置
 * 
 * @author yulinqu
 */
@Component
public class Config implements InitializingBean {

    @Value("${remote.file.host}")
    private String remoteFileHsot;

    @Value("${remote.file.path}")
    private String remoteFilePath;

    @Value("${remote.sever.username}")
    private String remoteSeverUserName;

    @Value("${remote.sever.pwd}")
    private String remoteSeverPwd;

    @Value("${local.file.path}")
    private String localFilePath;

    public String getRemoteFileHsot() {
        return remoteFileHsot;
    }

    public void setRemoteFileHsot(String remoteFileHsot) {
        this.remoteFileHsot = remoteFileHsot;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public String getRemoteSeverUserName() {
        return remoteSeverUserName;
    }

    public void setRemoteSeverUserName(String remoteSeverUserName) {
        this.remoteSeverUserName = remoteSeverUserName;
    }

    public String getRemoteSeverPwd() {
        return remoteSeverPwd;
    }

    public void setRemoteSeverPwd(String remoteSeverPwd) {
        this.remoteSeverPwd = remoteSeverPwd;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
