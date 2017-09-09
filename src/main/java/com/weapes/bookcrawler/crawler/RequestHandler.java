package com.weapes.bookcrawler.crawler;

import com.weapes.bookcrawler.extract.BookSearchWebPage;
import com.weapes.bookcrawler.request.Link;
import com.weapes.bookcrawler.store.Storable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by 不一样的天空 on 2017/6/9.
 */
public class RequestHandler<L extends Link> implements Runnable {
    private final ExecutorService creator;
    private final ExecutorService consumer;
    private final L link;
    RequestHandler(final ExecutorService creator,
                    final ExecutorService consumer,
                    final L link) {
        this.creator = creator;
        this.consumer = consumer;
        this.link = link;
    }
    @Override
    public void run() {
        try {
            BookSearchWebPage extractable= (BookSearchWebPage) link.request();
            if (extractable.hasMorePage()){
                extractable.loadNextPageLinks().forEach(paper ->
                        creator.submit(new RequestHandler<>(creator,consumer, paper)));
            }
            List<? extends Storable> bookList=extractable.extractAll();
            bookList.forEach(this::dispatch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将Storable交给消费者分发。
     * @param storable 被抽取后的Obj,可存储的对象
     */
    private void dispatch(final Storable storable) {
        consumer.submit(new StorableHandler<>(storable));
    }
}
