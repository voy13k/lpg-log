package me.voy13k.lpglog.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dao {

    private static Dao instance;

    public static synchronized Dao getInstance(Context context) {
        if (instance == null) {
            instance = new Dao(context);
        }
        return instance;
    }

    private static synchronized void close() {
        if (instance == null) {
            return;
        }
        if (instance.sqLiteDb != null) {
            instance.sqLiteDb.close();
            instance.sqLiteDb = null;
        }
        if (instance.openHelper != null) {
            instance.openHelper.close();
            instance.openHelper = null;
        }
        instance = null;
    }

    private SQLiteDatabase sqLiteDb;
    private SQLiteOpenHelper openHelper;

    public void save(FillUpEntry fillUpEntry) {
        ContentValues values = new ContentValues(5);
        long entryId = fillUpEntry.getId();
        if (entryId != 0) {
            values.put(FillUpEntry._ID, entryId);
        }
        values.put(FillUpEntry.COL_DATE, fillUpEntry.getDate());
        values.put(FillUpEntry.COL_DISTANCE, toInt(fillUpEntry.getDistance()*1000));
        values.put(FillUpEntry.COL_LPG_PRICE, toInt(fillUpEntry.getLpgPrice()*1000));
        values.put(FillUpEntry.COL_LPG_VOLUME, toInt(fillUpEntry.getLpgVolume()*1000));
        values.put(FillUpEntry.COL_ULP_PRICE, toInt(fillUpEntry.getUlpPrice()*1000));
        sqLiteDb.insertWithOnConflict(FillUpEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    private int toInt(Float f) {
        return f.intValue();
    }

    public interface OnLoadedListener<E> {
        void onLoaded(E entry);
    }

    public void loadFillUpEntries(OnLoadedListener<FillUpEntry> listener) {
        Cursor cursor = sqLiteDb.query(FillUpEntry.TABLE_NAME, FillUpEntry.COLS, null, null,
                null, null, FillUpEntry.COL_DATE + " desc");
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                listener.onLoaded(loadFillUpEntry(cursor));
            }
        } finally {
            cursor.close();
        }
    }

    private FillUpEntry loadFillUpEntry(Cursor cursor) {
        FillUpEntry entry = new FillUpEntry();
        entry.setId(cursor.getLong(0));
        entry.setDate(cursor.getLong(1));
        entry.setDistance(cursor.getInt(2)/1000f);
        entry.setLpgPrice(cursor.getInt(3)/1000f);
        entry.setLpgVolume(cursor.getInt(4)/1000f);
        entry.setUlpPrice(cursor.getInt(5)/1000f);
        return entry;
    }

    private Dao(Context context) {
        openHelper = new SQLiteOpenHelper(context, DbContract.DB_NAME, null,
                DbContract.DB_VERSION) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(FillUpEntry.TABLE_CREATE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                for (int version = oldVersion; version < newVersion; version++) {
                    for (String versionUpgradeSql : DbContract.UPGRADE_SQL[version]) {
                        db.execSQL(versionUpgradeSql);
                    }
                }
            }
        };
        sqLiteDb = openHelper.getWritableDatabase();
    }
}
