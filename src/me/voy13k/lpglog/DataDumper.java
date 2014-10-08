package me.voy13k.lpglog;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.data.FillUpEntry;

public class DataDumper implements DataStore.OnDataChangedListener {

    public static final String FILE_DUMP = "DataDump.csv";

    private final Context context;
    private final DataStore dataStore;

    public DataDumper(Context context, DataStore dataStore) {
        this.context = context;
        this.dataStore = dataStore;
        dataStore.register(this);
    }

    @Override
    public void onDataChanged() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new File(context.getExternalFilesDir(null), FILE_DUMP));
            writer.println("Date,Distance,LpgPrice,UlpPrice,LpgVolume");
            for (FillUpEntry entry: dataStore.getFillUpEntries()) {
                writer.format("%tF,%.1f,%.3f,%.3f,%.2f",
                        entry.getDate(),
                        entry.getDistance(),
                        entry.getLpgPrice(),
                        entry.getUlpPrice(),
                        entry.getLpgVolume()
                );
            }
        } catch (FileNotFoundException e) {
            Log.e("DataDumper", "Unable to dump the data to " + "Log.csv", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

}
