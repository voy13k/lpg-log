package me.voy13k.lpglog;

import me.voy13k.lpglog.util.ListenerRegistry;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private static final String LISTENER_KEY = "listenerKey";
	
	public static DatePickerFragment newInstance(String fragmentKey, int year, int month, int day) {
		Bundle args = new Bundle();
		args.putString(LISTENER_KEY, fragmentKey);
		args.putInt(YEAR, year);
		args.putInt(MONTH, month);
		args.putInt(DAY, day);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		int year = args.getInt(YEAR);
		int month = args.getInt(MONTH);
		int day = args.getInt(DAY);
		String fragmentKey = args.getString(LISTENER_KEY);
		OnDateSetListener listener = ListenerRegistry.get(getActivity(), fragmentKey);
		return new DatePickerDialog(getActivity(), listener, year, month, day);
	}

}
