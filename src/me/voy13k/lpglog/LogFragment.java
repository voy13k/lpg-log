package me.voy13k.lpglog;

import java.util.Arrays;

import me.voy13k.lpglog.data.Data;
import me.voy13k.lpglog.data.FillUpEntry;
import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LogFragment extends ListFragment {

	private static final int[] LOG_ITEM_VIEW_IDS = { R.id.date, R.id.gasConsupmtion, R.id.saving };
    private static final String[] CURSOR_COLUMN_NAMES = { BaseColumns._ID, "d", "c", "s" };
    private static final String[] COLUMN_NAMES = Arrays.copyOfRange(CURSOR_COLUMN_NAMES, 1,
        CURSOR_COLUMN_NAMES.length);

	private OnFragmentInteractionListener mListener;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LogFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new SimpleCursorAdapter(getActivity(),
				R.layout.list_item_log, getLogDataCursor(), COLUMN_NAMES,
				LOG_ITEM_VIEW_IDS, 0));
	}

	private Cursor getLogDataCursor() {
		MatrixCursor matrixCursor = new MatrixCursor(CURSOR_COLUMN_NAMES);
        for (FillUpEntry entry: Data.getInstance(getActivity()).getFillUpEntries()) {
            matrixCursor.newRow()
                    .add(entry.getId())
                    .add(Format.DATE.format(entry.getDate()))
                    .add(Format.CONSUMPTION.format(100 * entry.getLpgConsumption()))
                    .add(Format.DOLLARS.format(entry.getSaving()));
        }
        return matrixCursor;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (null != mListener) {
			mListener.onFillUpEntryClick(id);
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onFillUpEntryClick(long entryId);
	}

}
