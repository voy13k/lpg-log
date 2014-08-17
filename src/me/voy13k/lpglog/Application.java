package me.voy13k.lpglog;

import me.voy13k.lpglog.data.DataStore;

public class Application extends android.app.Application {

    private DataStore dataStore;

    public Application() {
        dataStore = new DataStore(this);
    }

    public DataStore getDataStore() {
        return dataStore;
    }
}
