package me.voy13k.lpglog.data;

import java.math.BigDecimal;
import java.util.Date;

import me.voy13k.lpglog.data.DbContract.FillUp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dao {

    private static Dao instance;

    private SQLiteDatabase sqLiteDb;

    public static synchronized Dao getInstance(Context c) {
        if (instance == null) {
            instance = new Dao(c);
        }
        return instance;
    }

    public static synchronized void close() {
        if (instance.sqLiteDb != null) {
            instance.sqLiteDb.close();
            instance.sqLiteDb = null;
            instance = null;
        }
    }

    public void save(FillUpEntry fillUpEntry) {
        ContentValues values = new ContentValues(5);
        values.put(FillUp.COL_DATE, fillUpEntry.getDate().getTime());
        values.put(FillUp.COL_DISTANCE, fillUpEntry.getDistance().toString());
        values.put(FillUp.COL_LPG_PRICE, fillUpEntry.getLpgPrice().toString());
        values.put(FillUp.COL_LPG_VOLUME, fillUpEntry.getLpgVolume().toString());
        values.put(FillUp.COL_ULP_PRICE, fillUpEntry.getUlpPrice().toString());
        sqLiteDb.insert(FillUp.TABLE_NAME, null, values);
    }

    FillUpEntry[] loadFillUpEntries() {
        Cursor cursor = sqLiteDb.query(FillUp.TABLE_NAME, FillUp.COLS, null, null, null, null,
                FillUp.COL_DATE + " desc");
        try {
            FillUpEntry[] entries = new FillUpEntry[cursor.getCount()];
            for(int i = 0;!cursor.isAfterLast();cursor.moveToNext(),i++) {
                entries[i] = readFillUpEntry(cursor);
            }
            return entries;
        } finally {
            cursor.close();
        }
    }

    private FillUpEntry readFillUpEntry(Cursor cursor) {
        FillUpEntry entry = new FillUpEntry();
        entry.setDate(new Date(cursor.getLong(cursor.getColumnIndex(FillUp.COL_DATE))));
        entry.setDistance(new BigDecimal(cursor.getString(cursor.getColumnIndex(FillUp.COL_DISTANCE))));
        entry.setLpgPrice(new BigDecimal(cursor.getString(cursor.getColumnIndex(FillUp.COL_LPG_PRICE))));
        entry.setLpgVolume(new BigDecimal(cursor.getString(cursor.getColumnIndex(FillUp.COL_LPG_VOLUME))));
        entry.setUlpPrice(new BigDecimal(cursor.getString(cursor.getColumnIndex(FillUp.COL_ULP_PRICE))));
        return entry;
    }

    private Dao(Context context) {
        SQLiteOpenHelper openHelper = new SQLiteOpenHelper(context, DbContract.DB_NAME, null,
                DbContract.DB_VERSION) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(FillUp.TABLE_CREATE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // Version 1 - no updates required
            }
        };
        try {
            sqLiteDb = openHelper.getWritableDatabase();
        } finally {
            openHelper.close();
        }
    }

}
