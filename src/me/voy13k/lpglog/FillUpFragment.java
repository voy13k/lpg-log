package me.voy13k.lpglog;

import java.util.Calendar;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.data.FillUpEntry;
import me.voy13k.lpglog.widget.DatePickerButton;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FillUpFragment extends Fragment {

    public static final String ARG_ENTRY_ID = "entryId";

    private DataStore dataStore;
    private DatePickerButton buttonDate;
    private EditText editDistance;
    private EditText editLpgPrice;
    private EditText editUlpPrice;
    private EditText editLpgVolume;
    private long entryId;

    public static FillUpFragment newInstance(long entryId) {
        FillUpFragment fillUpFragment = new FillUpFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ENTRY_ID, entryId);
        fillUpFragment.setArguments(args);
        return fillUpFragment;
    }

    public FillUpFragment() {
        // Do NOT overload the default constructor.
        // This is framework requirement.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fill_up, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonDate = (DatePickerButton) view.findViewById(R.id.buttonDate);
        editDistance = (EditText) view.findViewById(R.id.editDistance);
        editLpgPrice = (EditText) view.findViewById(R.id.editLpgPrice);
        editUlpPrice = (EditText) view.findViewById(R.id.editUlpPrice);
        editLpgVolume = (EditText) view.findViewById(R.id.editLpgVolume);
        entryId = getArguments().getLong(ARG_ENTRY_ID, 0);
        fillFromDb();
        editDistance.requestFocus();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataStore = ((Application) activity.getApplication()).getDataStore();
    }

    private void fillFromDb() {
        if (entryId == 0) {
            return;
        }
        FillUpEntry entry = dataStore.getFillUpEntryById(entryId);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(entry.getDate());
        buttonDate.setCalendar(cal);
        editDistance.setText(Format.DISTANCE.format(entry.getDistance()));
        editLpgPrice.setText(Format.CENTS.format(entry.getLpgPrice() * 100));
        editLpgVolume.setText(Format.VOLUME.format(entry.getLpgVolume()));
        editUlpPrice.setText(Format.CENTS.format(entry.getUlpPrice() * 100));
    }

    public boolean save() {
        if (!isFormDataValid()) {
            return false;
        }
        FillUpEntry entry = new FillUpEntry();
        entry.setId(entryId);
        entry.setDate(buttonDate.getCalendar().getTimeInMillis());
        entry.setDistance(getFloat(editDistance));
        entry.setLpgPrice(getFloat(editLpgPrice) / 100);
        entry.setLpgVolume(getFloat(editLpgVolume));
        entry.setUlpPrice(getFloat(editUlpPrice) / 100);
        dataStore.save(entry);
        return true;
    }

    private boolean isFormDataValid() {
        return isValid(editLpgVolume) && isValid(editUlpPrice) && isValid(editLpgPrice)
                && isValid(editDistance);
    }

    private boolean isValid(EditText editText) {
        try {
            return getFloat(editText) > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private float getFloat(TextView textView) {
        return Float.parseFloat(textView.getText().toString());
    }

}