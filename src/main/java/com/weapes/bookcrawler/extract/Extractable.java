package com.weapes.bookcrawler.extract;

import com.weapes.bookcrawler.store.Storable;

import java.util.List;

/**
 * Created by 不一样的天空 on 2017/6/7.
 */
public interface Extractable {
    Storable extract();
    List<? extends Storable> extractAll();
    boolean hasMorePage();
}
