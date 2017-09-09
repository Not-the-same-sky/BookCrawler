package com.weapes.bookcrawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weapes.bookcrawler.request.BookSearchLink;
import com.weapes.bookcrawler.store.Book;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public class Helper{
    //单次爬取图书总量
    private static AtomicInteger BOOK_NUMBERS = new AtomicInteger();
    private static final String  CONFIG_PATH="resources/config/dangdang.json";
    private static final JSONObject dangDangConfig=fileMapToJsonObject(CONFIG_PATH);
    private static final OkHttpClient OK_HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
    // 将配置文件映射为JSON对象
    private static JSONObject fileMapToJsonObject(final String filePath) {
        try {
            InputStream in = Helper.class.getClassLoader()
                    .getResourceAsStream(filePath);
            int length=in.available();
            byte [] bytes=new byte[length];
            in.read(bytes);
            return JSON.parseObject(new String(bytes,"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    // 获取配置文件的JSON对象
    public static JSONObject getDangDangConfig() {
        assert dangDangConfig != null;
        return dangDangConfig;
    }
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch(NumberFormatException e) {
            throw new NumberFormatException("PageIndex must be integer!");
        }
        return true;
    }
    private static List<String> getLinksFromConfig(){
         String baseUrl=dangDangConfig.getString("baseUrl");
         String act=dangDangConfig.getString("act");
         String pageIndex=dangDangConfig.getString("pageIndex");
         JSONArray keys=dangDangConfig.getJSONArray("keys");
         return keys.stream()
                 .map(key ->constructLink(baseUrl,act,pageIndex,key))
                 .collect(Collectors.toList());
    }
    private static String constructLink(String baseUrl,String act,String pageIndex,Object key){
        StringBuilder link=new StringBuilder();
       return link.append(baseUrl)
                .append("?key=").append(key)
                .append("&act=").append(act)
                .append("&page_index=").append(pageIndex).toString();
    }
    /**
     * 解析配置文件,获得原始种子
     *
     * @return seeds
     * @throws IOException dummyInfo
     */

    public static List<BookSearchLink> loadLinks() {
        System.out.println("开始加载种子...");
        final List<String> urls =
                getLinksFromConfig();

        System.out.println("种子加载完成...");

        return urls.stream()
                .map(BookSearchLink::new)
                .collect(Collectors.toList());
    }
    /**
     * 根据url下载网页。
     *
     * @param url 要下载的链接
     * @return 网页HTML
     * @throws IOException dummyInfo
     */
    public static String fetchWebPage(final String url)
            throws IOException {
        final Request request = new Request.Builder()
                .url(url)
//                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36")
//        .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//        .addHeader("Accept-Encoding","gzip, deflate, sdch")
//.addHeader("Accept-Language","zh-CN,zh;q=0.8")
//.addHeader("Cache-Control","max-age=0")
//.addHeader("Host","category.dangdang.com")
//.addHeader("Proxy-Connection","keep-alive")
//.addHeader("Upgrade-Insecure-Requests","1")
                .build();

        final Response executed = OK_HTTP_CLIENT.newCall(request)
                .execute();
        //System.out.println(executed.body().string());
        return executed.body().string();
    }
    public static List<String> getMoreLinksFromCurrentPageLink(String currentPageLink,int pageSum){
        List<String> links=new ArrayList<>();
        String baseUrl=currentPageLink.substring(0,currentPageLink.lastIndexOf("=")+1);
        for (int i = pageSum; i > 1 ; i--) {
            links.add(baseUrl+i);
        }
        return links;
    }

    public static AtomicInteger getBookNumbers() {
        return BOOK_NUMBERS;
    }

    public static void setBookNumbers(AtomicInteger bookNumbers) {
        BOOK_NUMBERS = bookNumbers;
    }

    public static void main(String []args) throws IOException {
        try {
            InputStream in = Helper.class.getClassLoader()
                    .getResourceAsStream("resources/config/type.txt");
            int length=in.available();
            byte [] bytes=new byte[length];
            in.read(bytes);
            String content = new String(bytes,"GBK");
            String [] types = content.replace("  "," ")
                    .replace("\r\n"," ").split(" ");
            StringBuilder result = new StringBuilder();
            for (String s:types){
                if (!s.equals(" ")){
                   // System.out.println(s);
                    result.append("\""+s+"\",");

                }

            }
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}