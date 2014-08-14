package me.voy13k.lpglog.data;

import android.provider.BaseColumns;
import android.text.TextUtils;

interface DbContract {

    String TYPE_NUMERIC = " NUMERIC";
    String DB_NAME = "LPGLog.db";
    int DB_VERSION = 3;

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

        interface V1 {
            // rename a upl_price to ulp_price
            String COL_UPL_PRICE = "upl_price";
        }
        
        // change data to be in common units
        String[] UPDATE_V1V2 = {
                "update " + TABLE_NAME + " set "
                + COL_DISTANCE + "=" + COL_DISTANCE + "/1000.0,"
                + COL_LPG_PRICE + "=" + COL_LPG_PRICE + "/1000.0,"
                + COL_LPG_VOLUME + "=" + COL_LPG_VOLUME + "/1000.0,"
                + V1.COL_UPL_PRICE + "=" + V1.COL_UPL_PRICE + "/1000.0;"
        };
        
        interface V2 {
            // rename a upl_price to ulp_price
            String TABLE_NAME = "fill_up_tmp";
            String[] COLS = {
                    _ID, COL_DATE, COL_DISTANCE, COL_LPG_PRICE, COL_LPG_VOLUME, V1.COL_UPL_PRICE
            };
            String COLS_LIST = TextUtils.join(",", COLS);
        }
        
        String[] UPDATE_V2V3 = {
                "alter table " + TABLE_NAME + " rename to " + V2.TABLE_NAME + ";",
                TABLE_CREATE,
                "insert into " + TABLE_NAME + " (" + COLS_LIST + ") select " + V2.COLS_LIST + " from " + V2.TABLE_NAME + ";",
                "drop table " + V2.TABLE_NAME + ";"
        };
    }

}
