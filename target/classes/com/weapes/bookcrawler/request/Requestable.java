package com.weapes.bookcrawler.request;

import com.weapes.bookcrawler.extract.Extractable;

import java.io.IOException;

/**
 * Created by 不一样的天空 on 2017/6/8.
 */
public interface Requestable<E extends Extractable> {
       E request() throws IOException;
}
