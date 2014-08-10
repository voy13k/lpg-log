package me.voy13k.lpglog.data;

public interface DbContract {

    String TYPE_NUMERIC = " NUMERIC";
    String SEP_COMMA = ",";

    String DB_NAME = "LPGLog.db";
    int DB_VERSION = 1;

    interface FillUp {
        String TABLE_NAME = "fill_up";

        String COL_DATE = "date";
        String COL_DISTANCE = "distance";
        String COL_LPG_PRICE = "lpg_price";
        String COL_LPG_VOLUME = "lpg_volume";
        String COL_ULP_PRICE = "upl_price";
        String[] COLS = {
                COL_DATE, COL_DISTANCE, COL_LPG_PRICE, COL_LPG_VOLUME, COL_ULP_PRICE
        };
        
        String TABLE_CREATE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + COL_DATE + " INTEGER PRIMARY KEY, "
                + COL_DISTANCE + TYPE_NUMERIC + SEP_COMMA
                + COL_LPG_PRICE + TYPE_NUMERIC + SEP_COMMA
                + COL_LPG_VOLUME + TYPE_NUMERIC + SEP_COMMA
                + COL_ULP_PRICE + TYPE_NUMERIC + ");";
    }

}