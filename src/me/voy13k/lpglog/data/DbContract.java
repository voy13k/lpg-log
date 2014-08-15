package me.voy13k.lpglog.data;

import android.provider.BaseColumns;
import android.text.TextUtils;

interface DbContract {

    String TYPE_NUMERIC = " NUMERIC";
    String DB_NAME = "LPGLog.db";
    int DB_VERSION = 1;

    interface FillUp extends BaseColumns {
        String TABLE_NAME = "fill_up";

        String COL_DATE = "date";
        String COL_DISTANCE = "distance";
        String COL_LPG_PRICE = "lpg_price";
        String COL_LPG_VOLUME = "lpg_volume";
        String COL_ULP_PRICE = "ulp_price";

        String[] COLS = {
                _ID, COL_DATE, COL_DISTANCE, COL_LPG_PRICE, COL_LPG_VOLUME, COL_ULP_PRICE
        };

        String COLS_LIST = TextUtils.join(",", COLS);

        String TABLE_CREATE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_DATE + TYPE_NUMERIC + ","
                + COL_DISTANCE + TYPE_NUMERIC + ","
                + COL_LPG_PRICE + TYPE_NUMERIC + ","
                + COL_LPG_VOLUME + TYPE_NUMERIC + ","
                + COL_ULP_PRICE + TYPE_NUMERIC + ");";

        // In upgrade / downgrade scripts don't use constants,
        // cause their current versions might not match the old ones.
        // Use hardcoded text that matches appropriate old versions.
        
        String[] V4V3 = { // change data to be decimals, rounded to 3 digits (km, litres, dollars)
                "update fill_up set "
                + "distance=round(distance/1000.0, 3),"
                + "lpg_price=round(lpg_price/1000.0, 3),"
                + "lpg_volume=round(lpg_volume/1000.0, 3),"
                + "ulp_price=round(ulp_price/1000.0, 3);",
        };
    }
    String[][] UPGRADE_SQL = {
            {},
    };
    String[][] DOWNGRADE_SQL = {
            {},
            {},
            {},
            {},
            FillUp.V4V3,
    };

}
