package com.meiliyun.analyser.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliyun.analyser.utils.ScpUtils;

/**
 * 每天定时去nginx服务器scp文件到本地
 * 
 * @author yulinqu
 */
public class ScpTask {

    @Autowired
    private Config config;

    public static final String DEFAULT_LOG_FILE_NAME = "access.log";

    public void scpFile() throws IOException {

        // 远程服务器的文件名
        String fileName = DEFAULT_LOG_FILE_NAME + getCurrentDate();

        String remoteServerIp = config.getRemoteFileHsot();
        String remoteSeverHostname = config.getRemoteSeverUserName();
        String remoteSeverPassword = config.getRemoteSeverPwd();
        String remoteFilePath = config.getRemoteFilePath() + fileName;
        String localPath = config.getLocalFilePath();
        // 1.从远程服务器 scp 文件到本地服务器
        ScpUtils.scpFileFromRemoteSever(remoteServerIp, remoteSeverHostname, remoteSeverPassword, remoteFilePath,
                localPath);
        // 复制到本地的日志文件
        File localFile = new File(localPath + fileName);
        if (!localFile.exists()) {
            return;
        }
        // 2.分析文件 按照每分钟的来统计 解析日志文件
        if (localFile.isFile() && localFile.exists()) { // 判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(localFile));// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] split = lineTxt.split("|");
                // 请求ip
                String ip = split[0];
                // 请求时间
                String requestTime = split[1];
                String reTime = null;
                if (StringUtils.isNotBlank(requestTime)) {
                    reTime = requestTime.split("/")[2].substring(0, 13);
                }
                // 埋点数据
                String data = split[2];
                if (StringUtils.isNotBlank(data) && data.contains("/img/behavior.gif?")) {
                    // 访问列表
                    if (data.contains("/img/behavior.gif??rnd=")) {

                    }
                    // 点击
                    else if (data.contains("/img/behavior.gif??node=")) {
                        int indexOf = data.indexOf("node=");
                    }

                }

                // uuid
                String uuid = split[10];

            }
            read.close();
        }

    }

    public String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        int YEAR = now.get(Calendar.YEAR);
        int MONTH = now.get(Calendar.MONTH);
        int DAY = now.get(Calendar.DAY_OF_MONTH);
        String dateStr = YEAR + "-" + (MONTH + 1) + "-" + DAY;

        return dateStr;
    }

}
