package me.voy13k.lpglog.data;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

/**
 * Long living wrapper object around data from DB. Clients can hold on to this
 * object. Some events might invalidate the data held inside. If a client wants
 * to hold on to the data returned from here, it should register itself as a
 * OnDataListener to receive the signal that data is no longer valid. They can
 * then retrieve new data when needed again.
 */
public class DataStore {

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    private static final String PREFERENCES = "LPGLog.prefs";

    private static final String PREF_LPG_CONVERSION_COST = "LpgConversionCost";
    private static final String PREF_AVG_ULP_CONSUMPTION = "AverageUlpConsumption";

    private static Format DB_FLOAT = new DecimalFormat("0.###");

    private Context context;
    private DbOpenHelper dbOpenHelper;
    private Data data;
    private SimpleArrayMap<OnDataChangedListener, OnDataChangedListener> listeners;

    public DataStore(Context context) {
        this.context = context;
        this.dbOpenHelper = new DbOpenHelper(context);
        this.listeners = new SimpleArrayMap<OnDataChangedListener, OnDataChangedListener>();
    }

    public void register(OnDataChangedListener listener) {
        this.listeners.put(listener, listener);
        Log.d("DataStore", "listeners: " + listeners.size());
    }
    
    public void deregister(OnDataChangedListener listener) {
        this.listeners.remove(listener);
        Log.d("DataStore", "listeners: " + listeners.size());
    }
    
    public List<FillUpEntry> getFillUpEntries() {
        return getData().fillUpEntries;
    }

    public float getAverageLpgConsumption() {
        Data data = getData();
        return 100 * data.totalLpgVolume / data.totalDistance;
    }

    public float getTotalSavings() {
        return getData().totalSavings;
    }

    public FillUpEntry getFillUpEntryById(long entryId) {
        for (FillUpEntry entry : getFillUpEntries()) {
            if (entry.getId() == entryId) {
                return entry;
            }
        }
        throw new IllegalArgumentException("No FillUpEntry with id=" + entryId);
    }

    public void save(FillUpEntry fillUpEntry) {
        ContentValues values = new ContentValues(5);
        long entryId = fillUpEntry.getId();
        if (entryId != 0) {
            values.put(FillUpEntry._ID, entryId);
        }
        values.put(FillUpEntry.COL_DATE, fillUpEntry.getDate());
        values.put(FillUpEntry.COL_DISTANCE, DB_FLOAT.format(fillUpEntry.getDistance()));
        values.put(FillUpEntry.COL_LPG_PRICE, DB_FLOAT.format(fillUpEntry.getLpgPrice()));
        values.put(FillUpEntry.COL_LPG_VOLUME, DB_FLOAT.format(fillUpEntry.getLpgVolume()));
        values.put(FillUpEntry.COL_ULP_PRICE, DB_FLOAT.format(fillUpEntry.getUlpPrice()));
        dbOpenHelper.getReadableDatabase().insertWithOnConflict(FillUpEntry.TABLE_NAME, null,
                values, SQLiteDatabase.CONFLICT_REPLACE);
        synchronized (this) {
            data = null;
        }
        notifyOnDataChange();
    }

    public float getLpgConversionCost() {
        return getPreferences().getFloat(PREF_LPG_CONVERSION_COST, 0);
    }

    public void setLpgConversionCost(float v) {
        getPreferences().edit().putFloat(PREF_LPG_CONVERSION_COST, v).commit();
        notifyOnDataChange();
    }

    public float getAverageUlpConsumption() {
        return getPreferences().getFloat(PREF_AVG_ULP_CONSUMPTION, 0);
    }

    public void setAverageUlpConsumption(float v) {
        getPreferences().edit().putFloat(PREF_AVG_ULP_CONSUMPTION, v).commit();
        deriveStatsAndTotals();
        notifyOnDataChange();
    }

    private synchronized Data getData() {
        if (data == null) {
            retrieveData();
        }
        return data;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private void retrieveData() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(FillUpEntry.TABLE_NAME, FillUpEntry.COLS, null, null, null,
                null, FillUpEntry.COL_DATE + " desc");
        data = new Data(cursor.getCount());
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                FillUpEntry entry = fromCursor(cursor);
                addToData(entry);
            }
        } finally {
            cursor.close();
        }
        deriveStatsAndTotals();
    }

    private void addToData(FillUpEntry entry) {
        data.fillUpEntries.add(entry);
        data.totalLpgVolume += entry.getLpgVolume();
        data.totalDistance += entry.getDistance();
    }

    private FillUpEntry fromCursor(Cursor cursor) {
        FillUpEntry entry = new FillUpEntry();
        entry.setId(cursor.getLong(0));
        entry.setDate(cursor.getLong(1));
        entry.setDistance(cursor.getFloat(2));
        entry.setLpgPrice(cursor.getFloat(3));
        entry.setLpgVolume(cursor.getFloat(4));
        entry.setUlpPrice(cursor.getFloat(5));
        return entry;
    }

    private void deriveStatsAndTotals() {
        float averageLpgConsumption = getAverageLpgConsumption();
        float averageUlpConsumption = getAverageUlpConsumption();
        float avgUlpToLpgRatio = averageUlpConsumption / averageLpgConsumption;
        data.totalSavings = 0f;

        for (FillUpEntry entry : data.fillUpEntries) {
            float lpgVolume = entry.getLpgVolume();
            float lpgCost = lpgVolume * entry.getLpgPrice();
            float ulpCost = lpgVolume * avgUlpToLpgRatio * entry.getUlpPrice();
            float saving = ulpCost - lpgCost;
            entry.setSaving(saving);
            data.totalSavings += saving;
        }
    }

    private static class Data {

        List<FillUpEntry> fillUpEntries;

        float totalDistance;
        float totalLpgVolume;
        float totalSavings;

        Data(int count) {
            fillUpEntries = new ArrayList<FillUpEntry>(count);
        }

    }
    
    private void notifyOnDataChange() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.keyAt(i).onDataChanged();
        }
    }
}
