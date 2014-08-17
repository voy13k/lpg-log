package me.voy13k.lpglog.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {
        super(context, DbContract.DB_NAME, null, DbContract.DB_VERSION);
    }

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
}