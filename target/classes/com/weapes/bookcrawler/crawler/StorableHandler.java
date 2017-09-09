package com.weapes.bookcrawler.crawler;


import com.weapes.bookcrawler.store.Storable;

/**
 * Created by lawrence on 16/8/8.
 */
class StorableHandler<S extends Storable> implements Runnable {

    private final S storable;

    StorableHandler(final S storable) {
        this.storable = storable;
    }

    @Override
    public void run() {
        storable.store();
    }
}
