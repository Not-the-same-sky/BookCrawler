package com.weapes.bookcrawler.store;

import com.weapes.bookcrawler.mapper.BookMapper;
import com.weapes.bookcrawler.util.Helper;
import com.weapes.bookcrawler.util.SqlHelper;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public class Book implements Storable{
    private String title;
    private String author;
    private String price;
    private String press;
    private String description;
    private String publishDate;
    private String imageUrl;
    private String type;
    private String amount;
    public Book(String title, String author, String price, String press,
                String description, String publishDate, String imageUrl,
                String type,String amount) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.press = press;
        this.description = description;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
        this.type = type;
        this.amount = amount;
    }
    public Book(){

    }
    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Book setPrice(String price) {
        this.price = price;
        return this;
    }

    public Book setPress(String press) {
        this.press = press;
        return this;
    }

    public Book setDescription(String description) {
        this.description = description;
        return this;
    }

    public Book setPublishDate(String publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public Book setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Book setType(String type) {
        this.type = type;
        return this;
    }
    public Book setAmount(String amount) {
        this.amount = amount;
        return this;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getPress() {
        return press;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }



    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price='" + price + '\'' +
                ", press='" + press + '\'' +
                ", description='" + description + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public void store() {
        System.err.println("*********"+this);
        SqlSession sqlSession = SqlHelper.openThreadSqlSession();
        BookMapper bookMapper=sqlSession.getMapper(BookMapper.class);
        bookMapper.saveBook(this);
        System.out.println("----------当前已保存"+
                Helper.getBookNumbers().addAndGet(1)+"篇图书信息");
    }
}
