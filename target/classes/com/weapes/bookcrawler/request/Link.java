package com.weapes.bookcrawler.request;

/**
 * Created by 不一样的天空 on 2017/6/8.
 */
public abstract class Link implements Requestable {
    private final String url;

    Link(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
