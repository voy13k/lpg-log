package me.voy13k.lpglog.data;

import java.util.Arrays;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

public class Data {

	private static final String[] CURSOR_COLUMN_NAMES = { BaseColumns._ID,
			"date", "gasConsumption", "savings" };

	public static final String[] COLUMN_NAMES = Arrays.copyOfRange(
			CURSOR_COLUMN_NAMES, 1, CURSOR_COLUMN_NAMES.length);

	public static Cursor getCursor() {
		MatrixCursor matrixCursor = new MatrixCursor(CURSOR_COLUMN_NAMES);
		matrixCursor.newRow().add("11").add("28/07/2014").add("13.4").add("19.53");
		matrixCursor.newRow().add("22").add("21/07/2014").add("13.2").add("18.53");
		matrixCursor.newRow().add("33").add("14/07/2014").add("13.0").add("17.53");
		matrixCursor.newRow().add("44").add("07/07/2014").add("13.0").add("16.53");
		matrixCursor.newRow().add("55").add("30/06/2014").add("13.1").add("15.53");
		matrixCursor.newRow().add("66").add("23/06/2014").add("12.7").add("14.53");
		matrixCursor.newRow().add("77").add("16/06/2014").add("13.2").add("13.53");
		matrixCursor.newRow().add("88").add("09/06/2014").add("13.0").add("12.53");
		matrixCursor.newRow().add("99").add("02/06/2014").add("13.1").add("11.53");
		return matrixCursor;
	}

}
