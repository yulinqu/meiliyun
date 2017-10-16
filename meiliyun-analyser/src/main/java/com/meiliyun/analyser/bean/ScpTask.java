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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliyun.analyser.dao.MeiliyunDAO;
import com.meiliyun.analyser.utils.ScpUtils;

/**
 * 每天定时去nginx服务器scp文件到本地
 * 
 * @author yulinqu
 */
public class ScpTask {

    @Autowired
    private Config config;

    @Autowired
    private MeiliyunDAO meiliyunDAO;

    public static final String DEFAULT_LOG_FILE_NAME = "access.log";

    public void scpFile() throws IOException, SQLException {

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

        // 每分钟的pv uv<time,<url,List<uuid>>>
        Map<String, Map<String, List<String>>> preMinCount = new HashMap<String, Map<String, List<String>>>();

        // 各产品每分钟的点击情况<time,<url,<position,Map<pid,count>>>>

        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preBannerClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();
        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preIconClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();
        Map<String, Map<String, Map<String, Map<String, List<String>>>>> preButtonClick = new HashMap<String, Map<String, Map<String, Map<String, List<String>>>>>();

        // Map<String, Map<String, List<String>>> preBannerClick = new HashMap<String, Map<String, List<String>>>();
        // Map<String, Map<String, List<String>>> preIconClick = new HashMap<String, Map<String, List<String>>>();
        // Map<String, Map<String, List<String>>> preButtonClick = new HashMap<String, Map<String, List<String>>>();

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
                    // 14/Oct/2017:06:06:48
                    String replace = requestTime.replace(" ", "");
                    int index = replace.lastIndexOf(":");

                    reTime = replace.substring(0, index);

                    // pvuv
                    if (preMinCount.get(reTime) == null) {
                        preMinCount.put(reTime, new HashMap<String, List<String>>());
                    }
                    // banner 统计
                    if (preBannerClick.get(reTime) == null) {
                        preBannerClick.put(reTime, new HashMap<String, Map<String, Map<String, List<String>>>>());
                    }
                    // icon 点击
                    if (preIconClick.get(reTime) == null) {
                        preIconClick.put(reTime, new HashMap<String, Map<String, Map<String, List<String>>>>());
                    }
                    // button 点击
                    if (preButtonClick.get(reTime) == null) {
                        preButtonClick.put(reTime, new HashMap<String, Map<String, Map<String, List<String>>>>());
                    }

                }
                // 埋点数据
                String data = split[2];
                if (StringUtils.isNotBlank(data) && data.contains("/img/behavior.gif?")) {
                    // uuid
                    String uuidOri = split[10];
                    // 取出 uuid
                    String uuid = uuidOri.substring(uuidOri.indexOf("UUID="), uuidOri.indexOf("UUID=") + 32);

                    // Referer 当前页url http://www.xianjinshijie.com:8090/show_list_page.do
                    // String referer = split[9];
                    String url = split[9].split("/")[1];

                    // 访问list_page
                    if (data.contains("/img/behavior.gif?rnd=")) {

                        if (StringUtils.isNotBlank(url)) {
                            if (preMinCount.get(reTime).get(url) != null) {
                                preMinCount.get(reTime).get(url).add(uuid);
                            } else {
                                preMinCount.get(reTime).put(url, new ArrayList<String>());
                                preMinCount.get(reTime).get(url).add(uuid);
                            }

                        }

                    }
                    // banner点击
                    else {
                        if (data.contains("/img/behavior.gif?node=")) {

                            int index = data.indexOf("msg=");
                            String pid_position = data.substring(index, index + 34);
                            String position = pid_position.split("-")[0];
                            String pid = pid_position.split("-")[1];

                            if (data.contains("/img/behavior.gif?node=banner")) {
                                // 各产品每分钟的点击情况<time,<url,<position,Map<pid,count>>>>
                                if (StringUtils.isNotBlank(url)) {
                                    if (preBannerClick.get(reTime).get(url) != null) {

                                        if (preBannerClick.get(reTime).get(url).get(position) != null) {

                                            if (preBannerClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preBannerClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preBannerClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preBannerClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preBannerClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preBannerClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                            // icon点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-icon")) {

                                if (StringUtils.isNotBlank(url)) {
                                    if (preIconClick.get(reTime).get(url) != null) {

                                        if (preIconClick.get(reTime).get(url).get(position) != null) {

                                            if (preIconClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preIconClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preIconClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preIconClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preIconClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preIconClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preIconClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preIconClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                            // button点击
                            else if (data.contains("/img/behavior.gif?node=merchant-list-button")) {

                                if (StringUtils.isNotBlank(url)) {
                                    if (preButtonClick.get(reTime).get(url) != null) {

                                        if (preButtonClick.get(reTime).get(url).get(position) != null) {

                                            if (preButtonClick.get(reTime).get(url).get(position).get(pid) != null) {
                                                preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            } else {
                                                preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                        new ArrayList<String>());
                                                preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                            }

                                        } else {

                                            preButtonClick.get(reTime).get(url).put(position,
                                                    new HashMap<String, List<String>>());
                                            preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                    new ArrayList<String>());
                                            preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                        }

                                    } else {
                                        preButtonClick.get(reTime).put(url,
                                                new HashMap<String, Map<String, List<String>>>());
                                        preButtonClick.get(reTime).get(url).put(position,
                                                new HashMap<String, List<String>>());
                                        preButtonClick.get(reTime).get(url).get(position).put(pid,
                                                new ArrayList<String>());
                                        preButtonClick.get(reTime).get(url).get(position).get(pid).add(pid);
                                    }

                                }

                            }
                        }
                    }

                }
            }
            read.close();
        }

        // 统计每分钟pv uv

        List<PvUv> pvuv = new ArrayList<PvUv>();

        if (!preMinCount.isEmpty()) {
            Set<String> keys = preMinCount.keySet();
            for (String key : keys) {
                Map<String, List<String>> urlMap = preMinCount.get(key);
                Set<String> urlKey = urlMap.keySet();
                for (String url : urlKey) {
                    List<String> datas = urlMap.get(url);
                    Set<String> uniqDatas = new HashSet<String>();
                    uniqDatas.addAll(datas);
                    pvuv.add(new PvUv(key, url, datas.size(), uniqDatas.size()));
                }

            }
        }

        // 插入每天的pvuv
        meiliyunDAO.insertPvuv(pvuv);

        // 统计每分钟不同主区域的点击情况

    }

    public String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        int YEAR = now.get(Calendar.YEAR);
        int MONTH = now.get(Calendar.MONTH);
        int DAY = now.get(Calendar.DAY_OF_MONTH);
        String dateStr = YEAR + "-" + (MONTH + 1) + "-" + DAY;

        return dateStr;
    }

    public String formateDate(String dataStr) {

        if (StringUtils.isNotBlank(dataStr)) {
            String[] split = dataStr.replace("/", ":").split(":");
            String day = split[0];
            String month = split[1];
            String year = split[2];
            String hour = split[3];
            String min = split[4];
            String second = split[5];

        }
        return null;

    }

    public static void main(String[] args) throws ParseException {

        System.out.println(UUID.randomUUID().toString().replace("-", "").length());

        Date date = new Date();
        System.out.println(date);

        System.out.println("000000005ef28a09015ef28fd5a50002".length());

        String datastr = "14/Oct/2017:06:06:48";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = formatter.parse(datastr);

    }

}
