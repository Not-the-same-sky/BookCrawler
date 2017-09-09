package com.weapes.bookcrawler.crawler;

import com.weapes.bookcrawler.util.Helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public class DangDangBookCrawler implements Crawler {
    //生产者消费者线程数,可以根据环境进行调整
    private static final int CREATOR_THREAD_NUM = 1;
    private static final int CONSUMER_THREAD_NUM = 1;
    /*
     * 生产者负责把Requestable解析为Storable,
     * 消费者负责把Storable存储到数据库。
     */
    private static final ExecutorService CREATOR =
            Executors.newFixedThreadPool(CREATOR_THREAD_NUM);
    private static final ExecutorService CONSUMER =
            Executors.newFixedThreadPool(CONSUMER_THREAD_NUM);
    public void crawl() {
        System.out.println("爬虫开始运行.....");
        // 对每个链接,交给生产者处理为Storable
        Helper.loadLinks().forEach(paper ->
                CREATOR.submit(new RequestHandler<>(CREATOR, CONSUMER, paper)));
    }

    public static void main(String[]args){
        new DangDangBookCrawler().crawl();
    }
}
