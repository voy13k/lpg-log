package me.voy13k.lpglog;

import me.voy13k.lpglog.data.DataStore;

public class Application extends android.app.Application {

    private DataStore dataStore;

    public Application() {
        dataStore = new DataStore(this);
        new DataDumper(this, dataStore);
    }

    public DataStore getDataStore() {
        return dataStore;
    }
}
