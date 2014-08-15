package me.voy13k.lpglog.data;

import java.text.DecimalFormat;
import java.text.Format;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dao {

    private static Dao instance;
    private static Format DB_FLOAT = new DecimalFormat("0.###");

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
        values.put(FillUpEntry.COL_DISTANCE, DB_FLOAT.format(fillUpEntry.getDistance()));
        values.put(FillUpEntry.COL_LPG_PRICE, DB_FLOAT.format(fillUpEntry.getLpgPrice()));
        values.put(FillUpEntry.COL_LPG_VOLUME, DB_FLOAT.format(fillUpEntry.getLpgVolume()));
        values.put(FillUpEntry.COL_ULP_PRICE, DB_FLOAT.format(fillUpEntry.getUlpPrice()));
        sqLiteDb.insertWithOnConflict(FillUpEntry.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
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
        entry.setDistance(cursor.getFloat(2));
        entry.setLpgPrice(cursor.getFloat(3));
        entry.setLpgVolume(cursor.getFloat(4));
        entry.setUlpPrice(cursor.getFloat(5));
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

            @Override
            public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                for (int version = oldVersion; version > newVersion; version--) {
                    for (String versionDowngradeSql : DbContract.DOWNGRADE_SQL[version]) {
                        db.execSQL(versionDowngradeSql);
                    }
                }
            }
        };
        sqLiteDb = openHelper.getWritableDatabase();
    }
}
