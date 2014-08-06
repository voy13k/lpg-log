package me.voy13k.lpglog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FillUpFragment extends Fragment implements OnDateSetListener {

	private static final DateFormat DATE_FRMAT = SimpleDateFormat.getDateInstance();
	private static final String EDIT_DATE_LISTENER = "fillUpEditDateListener";
	private TextView editDateTextView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		ListenerRegistry.register(activity, EDIT_DATE_LISTENER, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				setText(editDateTextView, new GregorianCalendar(year, month, day));
			}
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();
		ListenerRegistry.deregister(getActivity(), EDIT_DATE_LISTENER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_fill_up, container,
				false);
		editDateTextView = (TextView) rootView.findViewById(R.id.editDate);
		initDatePicker(editDateTextView, Calendar.getInstance());
		return rootView;
	}

	private void initDatePicker(TextView textView, final Calendar cal) {
		final int year = cal.get(Calendar.YEAR);
		final int month = cal.get(Calendar.MONTH);
		final int day = cal.get(Calendar.DAY_OF_MONTH);
		textView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				DatePickerFragment.newInstance(EDIT_DATE_LISTENER, year, month, day)
						.show(getActivity().getSupportFragmentManager(), "blah");
			}
		});
		setText(textView, cal);
	}

	private void setText(TextView textView, final Calendar cal) {
		textView.setText(DATE_FRMAT.format(cal.getTime()));
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Log.i("FillUpFragment", "he he: " + view.getTag() + ":" + year + "" + monthOfYear + ""
				+ dayOfMonth);
	}

}