package me.voy13k.lpglog;

import android.content.Context;

/**
 * Workaround for https://code.google.com/p/android/issues/detail?id=34833
 */
public class DatePickerDialog extends android.app.DatePickerDialog {

	public DatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	protected void onStop() {
		 // Workaround for https://code.google.com/p/android/issues/detail?id=34833
	}
}
