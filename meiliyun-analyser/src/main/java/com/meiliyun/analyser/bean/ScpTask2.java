package com.meiliyun.analyser.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliyun.analyser.dao.MeiliyunDAO;
import com.meiliyun.analyser.utils.ScpUtils;

/**
 * 每天定时去nginx服务器scp文件到本地
 * 
 * @author yulinqu
 */
public class ScpTask2 {

    @Autowired
    private Config config;

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    public static final String DEFAULT_LOG_FILE_NAME = "access-";

    public static final String DEFAULT_LOG_FILE_NAME_SUFFIX = ".log";

    public static String TEST_DATA = null;

    private static Logger LOGGER = Logger.getLogger(ScpTask2.class);

    public String scpFileForTest(String data) {
        TEST_DATA = data;
        try {
            scpFile();
            TEST_DATA = null;
            return "SUCCESS";
        } catch (Exception e) {
            LOGGER.error("scp file erroe !", e);
            TEST_DATA = null;
            return "ERROR : " + e.getMessage();
        }

    }

    public void scpFile() throws IOException, SQLException {

        // 远程服务器的文件名
        String fileName = DEFAULT_LOG_FILE_NAME + getCurrentDate() + DEFAULT_LOG_FILE_NAME_SUFFIX;
        if (StringUtils.isNotBlank(TEST_DATA)) {
            fileName = DEFAULT_LOG_FILE_NAME + TEST_DATA + DEFAULT_LOG_FILE_NAME_SUFFIX;
        }

        String remoteServerIp = config.getRemoteFileHsot();
        String remoteSeverHostname = config.getRemoteSeverUserName();
        String remoteSeverPassword = config.getRemoteSeverPwd();
        String remoteFilePath = config.getRemoteFilePath() + fileName;
        String localPath = config.getLocalFilePath();

        File file = new File(localPath + fileName);

        if (file.exists()) {
            return;
        }

        // 1.从远程服务器 scp 文件到本地服务器
        ScpUtils.scpFileFromRemoteSever(remoteServerIp, remoteSeverHostname, remoteSeverPassword, remoteFilePath,
                localPath);
        LOGGER.info("from remote server scp file : " + fileName + " success !");
        // 复制到本地的日志文件
        File localFile = new File(localPath + fileName);
        LOGGER.info("start to analyser log : " + fileName);
        if (!localFile.exists()) {
            return;
        }

        // 每天点击量
        List<PvUv2> pvuvs = new ArrayList<PvUv2>();
        // 每天点击
        List<ProductClickBean2> clicks = new ArrayList<ProductClickBean2>();
        // 2.分析文件 按照每分钟的来统计 解析日志文件
        if (localFile.isFile() && localFile.exists()) { // 判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(localFile));// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                // 去除日志中的空格
                String[] split = lineTxt.replace(" ", "").split("\\|");
                // 请求ip
                String ip = split[0];
                // 请求时间
                String requestTime = split[1];
                String reTime = null;
                if (StringUtils.isNotBlank(requestTime)) {
                    // 14/Oct/2017:06:06:48
                    reTime = formateDate(requestTime);
                }
                // 埋点数据
                String data = split[2];
                if (StringUtils.isNotBlank(data) && data.contains("/img/behavior.gif?")) {
                    // uuid
                    String uuidOri = split[9].replace(" ", "");
                    String uuid = "UUID";
                    if (StringUtils.isNotBlank(uuidOri)) {
                        uuid = uuidOri;
                    }

                    // Referer 当前页url https://www.xianjinshijie.com/show_list_page.do?channel=dx
                    String referer = split[8].replaceAll(" ", "").replace("://", "");
                    // 请求url
                    String url = "show_list_page.do";
                    String[] refererSplit = referer.split("/");
                    String onlineUrl = refererSplit[1];
                    String[] onlineUrlsplit = onlineUrl.split("\\?");
                    // 渠道
                    String channel = null;
                    if (StringUtils.isNotBlank(onlineUrlsplit[0])) {
                        url = onlineUrlsplit[0];
                        if (StringUtils.isNotBlank(onlineUrlsplit[1])) {
                            String[] parameters = onlineUrlsplit[1].split("&");
                            for (String parameter : parameters) {
                                String[] parameterSplite = parameter.split("=");
                                if ("channel".equals(parameterSplite[0])) {
                                    channel = parameterSplite[1];
                                }
                            }
                        }

                    }
                    // 访问list_page
                    if (data.contains("/img/behavior.gif?rnd=")) {
                        pvuvs.add(new PvUv2(reTime, url, uuid, channel, ip));
                    }
                    // banner点击
                    else {
                        if (data.contains("/img/behavior.gif?node=")) {
                            int index = data.indexOf("msg=");
                            String pid_position = data.substring(index + 4, index + 38);
                            String position = "1";
                            String pid = "";
                            if (pid_position.indexOf("-") != -1) {
                                position = pid_position.split("-")[0];
                                pid = pid_position.split("-")[1];
                            } else {
                                pid = data.substring(index + 4, index + 36);
                            }
                            if (data.contains("/img/behavior.gif?node=banner")) {
                                // banner 统计
                                if (data.contains("/img/behavior.gif?node=banner-new")) {
                                    url = url + "_new";
                                }
                                clicks.add(new ProductClickBean2(reTime, url, "banner", position, pid, channel, ip));

                            }
                            // icon点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-icon")) {

                                if (data.contains("/img/behavior.gif?node=merchant-list-icon-new")) {
                                    url = url + "_new";
                                }
                                clicks.add(new ProductClickBean2(reTime, url, "icon", position, pid, channel, ip));

                            }
                            // button点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-button")) {
                                if (data.contains("/img/behavior.gif?node=merchant-list-button-new")) {
                                    url = url + "_new";
                                }
                                clicks.add(new ProductClickBean2(reTime, url, "button", position, pid, channel, ip));
                            }
                        }
                    }

                }
            }
            read.close();
        }

        // 插入每天的pvuv
        if (CollectionUtils.isNotEmpty(pvuvs)) {
            meiliyunDAO.insertPvuv2(pvuvs);
        }
        // 每天点击
        if (CollectionUtils.isNotEmpty(clicks)) {
            meiliyunDAO.insertPclick2(clicks);
        }
        LOGGER.info("end to analyser log : " + fileName);
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(time);

        return dateStr;
    }

    public String formateDate(String dataStr) {

        if (StringUtils.isNotBlank(dataStr)) {
            String replaceAll = dataStr.replaceAll("Jan", "01").replaceAll("Feb", "02").replaceAll("Mar", "03")
                    .replaceAll("Apr", "04").replaceAll("May", "05").replaceAll("Jun", "06").replaceAll("Jul", "07")
                    .replaceAll("Aug", "08").replaceAll("Sep", "09").replaceAll("Oct", "10").replaceAll("Nov", "11")
                    .replaceAll("Dec", "12");

            String[] split = replaceAll.replaceAll("/", ":").replaceAll(" ", "").split(":");
            String day = split[0];
            String month = split[1];
            String year = split[2];
            String hour = split[3];
            String min = split[4];
            String second = split[5];
            return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
        }
        return null;

    }

    public static void main(String[] args) throws ParseException, IOException, SQLException {

        System.out.println("https://www.xianjinshijie.com:8443/show_list_page.do".split("8443/")[1]);

        Calendar cal = Calendar.getInstance();
        // System.out.println(Calendar.DATE);//5
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(time));

        String referer = "https://www.xianjinshijie.com/show_list_page.do?channel=dx".replaceAll(" ", "").replace("://",
                "");
        String url = "show_list_page.do";
        String[] refererSplit = referer.split("/");
        String onlineUrl = refererSplit[1];
        String[] onlineUrlsplit = onlineUrl.split("\\?");
        // 渠道
        String channel = null;
        if (StringUtils.isNotBlank(onlineUrlsplit[0])) {
            url = onlineUrlsplit[0];
            if (StringUtils.isNotBlank(onlineUrlsplit[1])) {
                String[] parameters = onlineUrlsplit[1].split("&");
                for (String parameter : parameters) {
                    String[] parameterSplite = parameter.split("=");
                    if ("channel".equals(parameterSplite[0])) {
                        channel = parameterSplite[1];
                    }
                }
            }

        }

        System.out.println(url);
        System.out.println(channel);

    }

}
