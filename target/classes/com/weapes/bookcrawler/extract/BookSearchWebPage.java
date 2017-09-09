package com.weapes.bookcrawler.extract;

import com.weapes.bookcrawler.request.BookSearchLink;
import com.weapes.bookcrawler.store.Book;
import com.weapes.bookcrawler.store.Storable;
import com.weapes.bookcrawler.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 不一样的天空 on 2017/6/8.
 */
public class BookSearchWebPage extends WebPage {
    // 图书信息抽取CSS选择器
    private static final String BASE_CSS_SELECTOR =
            "#component_0__0__6612 > li";
    private static final String TITLE_CSS_SELECTOR =
            "p.name > a";
    private static final String AUTHOR_CSS_SELECTOR =
            "p.search_book_author >span:nth-child(1)";
    private static final String PRESS_CSS_SELECTOR =
            "p.search_book_author >span:nth-child(3)";
    private static final String PUBLISH_DATE_CSS_SELECTOR =
            "p.search_book_author >span:nth-child(2)";
    private static final String URL_CSS_SELECTOR =
            "a >img";
    private static final String PRICE_CSS_SELECTOR =
            "p.price >span.search_now_price";
    private static final String DESCRIPTION_CSS_SELECTOR =
            "p.detail";
    private static final String PAGE_SUM_CSS_SELECTOR =
            "#go_sort > div > div.data > span:nth-child(3)";
    // 网页的文档对象
    private static Document doc;
    private static int pageSum;

    public BookSearchWebPage(final String text, final String url) {
        super(text, url);
    }

    @Override
    public Storable extract() {
        System.out.println("链接解析: url=" + getUrl() + " type=AdvSearched");
        doc = Jsoup.parse(getText());
        return new Book().setTitle(parseTitle(doc))
                .setAuthor(parseAuthor(doc))
                .setPrice(parsePrice(doc))
                .setPress(parsePress(doc))
                .setPublishDate(parsePublishDate(doc))
                .setDescription(parseDescription(doc))
                .setImageUrl(parseImageUrl(doc))
                .setType(parseType(getUrl()));
    }

    @Override
    public List<? extends Storable> extractAll() {
        System.out.println("链接解析: url=" + getUrl() + " type=AdvSearched");
        doc = Jsoup.parse(getText());
        pageSum = parsePageSum(doc);
        final Elements elements = doc.select(BASE_CSS_SELECTOR);
        List<Book> books = new ArrayList<>();
        for (Element element : elements) {
            Book book = new Book()
                    .setTitle(parseTitle(element))
                    .setAuthor(parseAuthor(element))
                    .setImageUrl(parseImageUrl(element))
                    .setDescription(parseDescription(element))
                    .setPress(parsePress(element))
                    .setPublishDate(parsePublishDate(element))
                    .setType(parseType(getUrl()))
                    .setPrice(parsePrice(element))
                    .setAmount("100");
            books.add(book);
        }
        return books;
    }

    @Override
    public boolean hasMorePage() {
        return Integer.parseInt(parsePageIndex(getUrl())) == 1
                && pageSum > 1;
    }

    private int parsePageSum(final Document document) {
        pageSum = Integer.parseInt(
                document.select(PAGE_SUM_CSS_SELECTOR).text().substring(1));
        return pageSum;
    }

    private String parseTitle(final Element element) {
        return element.select(TITLE_CSS_SELECTOR).first().attr("title");
    }

    private String parseAuthor(final Element element) {
        return element.select(AUTHOR_CSS_SELECTOR).first().text();
    }

    private String parseDescription(final Element element) {
        return element.select(DESCRIPTION_CSS_SELECTOR).first().text();
    }

    private String parsePress(final Element element) {
        return element.select(PRESS_CSS_SELECTOR).first().text();
    }

    private String parsePublishDate(final Element element) {
        return element.select(PUBLISH_DATE_CSS_SELECTOR).first().text();
    }

    private String parsePrice(final Element element) {
        return element.select(PRICE_CSS_SELECTOR).first().text();
    }

    private String parseImageUrl(final Element element) {
            String src=element.select(URL_CSS_SELECTOR).first().attr("src");
            String original=element.select(URL_CSS_SELECTOR).first().attr("data-original");
        return src.contains("http") ? src : original;
    }

    private String parseType(final String url) {
        return url.substring(url.indexOf("=") + 1, url.indexOf("&"));
    }

    private String parsePageIndex(final String url) {
        return url.substring(url.lastIndexOf("=") + 1);
    }

    public List<BookSearchLink> loadNextPageLinks() {
        final List<String> urls = Helper.getMoreLinksFromCurrentPageLink(getUrl(), pageSum);
        return urls.stream()
                .map(BookSearchLink::new)
                .collect(Collectors.toList());
    }
}
