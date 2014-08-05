package me.voy13k.lpglog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;

/**
 * A placeholder fragment containing a simple view.
 */
public class FillUpFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_fill_up, container, false);
		v.findViewById(R.id.editDate).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Dialog dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						Log.i("FillUpFragment", "" + dayOfMonth + "/" + monthOfYear + "/" + year);
					}
				}, 2014, 7, 5) {
					@Override
					protected void onStop() {
						// Workaround for https://code.google.com/p/android/issues/detail?id=34833
					}
				};
				dialog.show();
			}
		});
		return v;
	}

}