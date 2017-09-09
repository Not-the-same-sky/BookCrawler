package com.weapes.bookcrawler.extract;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public abstract class WebPage extends ExtractedObject implements Extractable{
    private String url;
    private String text;

    public WebPage(final String text , final String url) {
        this.text = text;
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "url='" + url + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
