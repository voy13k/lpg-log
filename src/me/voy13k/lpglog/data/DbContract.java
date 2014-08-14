package me.voy13k.lpglog.data;

import android.provider.BaseColumns;

interface DbContract {

    String TYPE_NUMERIC = " NUMERIC";
    String SEP_COMMA = ",";

    String DB_NAME = "LPGLog.db";
    int DB_VERSION = 2;

    interface FillUp extends BaseColumns {
        String TABLE_NAME = "fill_up";

        String COL_DATE = "date";
        String COL_DISTANCE = "distance";
        String COL_LPG_PRICE = "lpg_price";
        String COL_LPG_VOLUME = "lpg_volume";
        String COL_ULP_PRICE = "upl_price";

        String[] COLS = {
                _ID, COL_DATE, COL_DISTANCE, COL_LPG_PRICE, COL_LPG_VOLUME, COL_ULP_PRICE
        };
        
        String TABLE_CREATE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COL_DATE + TYPE_NUMERIC + SEP_COMMA
                + COL_DISTANCE + TYPE_NUMERIC + SEP_COMMA
                + COL_LPG_PRICE + TYPE_NUMERIC + SEP_COMMA
                + COL_LPG_VOLUME + TYPE_NUMERIC + SEP_COMMA
                + COL_ULP_PRICE + TYPE_NUMERIC + ");";

        String UPDATE_1_2 =
                "update " + TABLE_NAME + " set "
                        + COL_DISTANCE + "=" + COL_DISTANCE + "/1000.0,"
                        + COL_LPG_PRICE + "=" + COL_LPG_PRICE + "/1000.0,"
                        + COL_LPG_VOLUME + "=" + COL_LPG_VOLUME + "/1000.0,"
                        + COL_ULP_PRICE + "=" + COL_ULP_PRICE + "/1000.0;";
    }

}
