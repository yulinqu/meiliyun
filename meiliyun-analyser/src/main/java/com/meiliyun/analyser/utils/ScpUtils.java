package com.meiliyun.analyser.utils;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

/**
 * scp 工具类
 * 
 * @author yulinqu
 */
public class ScpUtils {

    /**
     * 从远程服务器scp单个文件到本地
     * 
     * @param remoteServerIp
     * @param remoteSeverHostname
     * @param remoteFilePath
     * @param localPath
     * @throws IOException
     */
    public static void scpFileFromRemoteSever(String remoteServerIp, String remoteSeverHostname,
            String remoteSeverPassword, String remoteFilePath, String localPath) throws IOException {
        if (StringUtils.isNotBlank(remoteServerIp) && StringUtils.isNotBlank(remoteSeverHostname)
                && StringUtils.isNotBlank(remoteFilePath) && StringUtils.isNotBlank(localPath)) {
            Connection conn = new Connection(remoteServerIp);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(remoteSeverHostname, remoteSeverPassword);
            if (isAuthenticated == true) {
                SCPClient client = new SCPClient(conn);
                client.get(remoteFilePath, localPath);
                conn.close();
            }
        } else {
            return;
        }

    }

    /**
     * 从远程服务器多个文件到本地
     * 
     * @param remoteServerIp
     * @param remoteSeverHostname
     * @param remoteSeverPassword
     * @param remoteFilePaths
     * @param localPath
     * @throws IOException
     */
    public static void scpFilesFromRemoteSever(String remoteServerIp, String remoteSeverHostname,
            String remoteSeverPassword, String[] remoteFilePaths, String localPath) throws IOException {
        if (StringUtils.isNotBlank(remoteServerIp) && StringUtils.isNotBlank(remoteSeverHostname)
                && remoteFilePaths != null && StringUtils.isNotBlank(localPath)) {
            Connection conn = new Connection(remoteServerIp);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(remoteSeverHostname, remoteSeverPassword);
            if (isAuthenticated == true) {
                SCPClient client = new SCPClient(conn);
                client.get(remoteFilePaths, localPath);
                conn.close();
            }
        } else {
            return;
        }

    }

}
