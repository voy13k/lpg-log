package me.voy13k.lpglog;

import java.util.Calendar;

import me.voy13k.lpglog.data.Dao;
import me.voy13k.lpglog.data.Data;
import me.voy13k.lpglog.data.FillUpEntry;
import me.voy13k.lpglog.widget.DatePickerButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FillUpFragment extends Fragment {

    public static final String ARG_ENTRY_ID = "entryId";

    private DatePickerButton buttonDate;
    private EditText editDistance;
    private EditText editLpgPrice;
    private EditText editUlpPrice;
    private EditText editLpgVolume;
    private long entryId;

    public static FillUpFragment newInstance(Long entryId) {
        Bundle args = new Bundle();
        if (entryId != null) {
            args.putLong(ARG_ENTRY_ID, entryId);
        }
        FillUpFragment fillUpFragment = new FillUpFragment();
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
        buttonDate = (DatePickerButton) rootView.findViewById(R.id.buttonDate);
        editDistance = (EditText) rootView.findViewById(R.id.editDistance);
        editLpgPrice = (EditText) rootView.findViewById(R.id.editLpgPrice);
        editUlpPrice = (EditText) rootView.findViewById(R.id.editUlpPrice);
        editLpgVolume = (EditText) rootView.findViewById(R.id.editLpgVolume);
        entryId = getArguments().getLong(ARG_ENTRY_ID, 0);
        fillFromDb();
        editDistance.requestFocus();
        return rootView;
    }

    private void fillFromDb() {
        if (entryId == 0) {
            return;
        }
        for (FillUpEntry entry: Data.getInstance(getActivity()).getFillUpEntries()) {
            if (entry.getId() == entryId) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(entry.getDate());
                buttonDate.setCalendar(cal);
                editDistance.setText(Format.DISTANCE.format(entry.getDistance()));
                editLpgPrice.setText(Format.CENTS.format(entry.getLpgPrice() * 100));
                editLpgVolume.setText(Format.VOLUME.format(entry.getLpgVolume()));
                editUlpPrice.setText(Format.CENTS.format(entry.getUlpPrice() * 100));
                break;
            }
        }
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
        Dao.getInstance(getActivity()).save(entry);
        Data.reload();
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