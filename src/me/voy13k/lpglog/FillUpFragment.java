package me.voy13k.lpglog;

import me.voy13k.lpglog.util.ListenerRegistry;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
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
public class FillUpFragment extends Fragment implements OnDateSetListener {

	private static final String EDIT_DATE_LISTENER = "fillUpEditDateListener";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ListenerRegistry.register(activity, EDIT_DATE_LISTENER, this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		ListenerRegistry.deregister(getActivity(), EDIT_DATE_LISTENER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_fill_up, container, false);
		v.findViewById(R.id.editDate).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DatePickerFragment.newInstance(EDIT_DATE_LISTENER, 2012, 12, 12).show(
						getActivity().getSupportFragmentManager(), "blah");
			}
		});
		return v;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Log.i("FillUpFragment", "he he: " + year + "" + monthOfYear + ""
				+ dayOfMonth);
	}

}