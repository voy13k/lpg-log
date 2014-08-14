package me.voy13k.lpglog.data;

import android.provider.BaseColumns;
import android.text.TextUtils;

interface DbContract {

    String TYPE_NUMERIC = " NUMERIC";
    String DB_NAME = "LPGLog.db";
    int DB_VERSION = 4;

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
            String COL_UPL_PRICE = "upl_price";
        }
        
        interface V2 {
            String TABLE_NAME = "fill_up_tmp";
            String[] COLS = {
                    _ID, COL_DATE, COL_DISTANCE, COL_LPG_PRICE, COL_LPG_VOLUME, V1.COL_UPL_PRICE
            };
            String COLS_LIST = TextUtils.join(",", COLS);
        }
        
        String[] V1V2 = { // change data to be in common units
                "update " + TABLE_NAME + " set "
                + COL_DISTANCE + "=" + COL_DISTANCE + "/1000.0,"
                + COL_LPG_PRICE + "=" + COL_LPG_PRICE + "/1000.0,"
                + COL_LPG_VOLUME + "=" + COL_LPG_VOLUME + "/1000.0,"
                + V1.COL_UPL_PRICE + "=" + V1.COL_UPL_PRICE + "/1000.0;",
        };
        String[] V2V3 = { // rename a upl_price to ulp_price
                "alter table " + TABLE_NAME + " rename to " + V2.TABLE_NAME + ";"
                ,TABLE_CREATE
                ,"insert into " + TABLE_NAME + " (" + COLS_LIST + ") select " + V2.COLS_LIST + " from " + V2.TABLE_NAME + ";"
                ,"drop table " + V2.TABLE_NAME + ";"
        };
        String[] V3V4 = { // change data back to be ints (meters, millilitres, millidollars)
                "update " + TABLE_NAME + " set "
                + COL_DISTANCE + "=round(" + COL_DISTANCE + "*1000),"
                + COL_LPG_PRICE + "=round(" + COL_LPG_PRICE + "*1000),"
                + COL_LPG_VOLUME + "=round(" + COL_LPG_VOLUME + "*1000),"
                + COL_ULP_PRICE + "=round(" + COL_ULP_PRICE + "*1000.0);",
        };
    }
    String[][] UPGRADE_SQL = {
            {},
            FillUp.V1V2,
            FillUp.V2V3,
            FillUp.V3V4,
    };

}
