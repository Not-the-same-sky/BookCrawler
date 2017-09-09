package com.weapes.bookcrawler.request;

import com.weapes.bookcrawler.extract.BookSearchWebPage;
import com.weapes.bookcrawler.extract.Extractable;
import com.weapes.bookcrawler.extract.ExtractedObject;
import com.weapes.bookcrawler.util.Helper;

import java.io.IOException;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public class BookSearchLink extends Link {
    public BookSearchLink(final String url) {
        super(url);
    }

    @Override
    public Extractable request() throws IOException {
        final String bookSearchPage =
                Helper.fetchWebPage(getUrl());
        return new BookSearchWebPage(bookSearchPage, getUrl());
    }

}
