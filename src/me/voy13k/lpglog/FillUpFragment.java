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
                editDistance.setText(String.valueOf(entry.getDistance() / 1000.0));
                editLpgPrice.setText(String.valueOf(entry.getLpgPrice() / 10.0));
                editLpgVolume.setText(String.valueOf(entry.getLpgVolume() / 1000.0));
                editUlpPrice.setText(String.valueOf(entry.getUlpPrice() / 10.0));
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
        entry.setDistance(toInt(getDouble(editDistance) * 1000));
        entry.setLpgPrice(toInt(getDouble(editLpgPrice) * 10));
        entry.setLpgVolume(toInt(getDouble(editLpgVolume) * 1000));
        entry.setUlpPrice(toInt(getDouble(editUlpPrice) * 10));
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
            return getDouble(editText) > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int toInt(Double d) {
        return d.intValue();
    }

    private double getDouble(TextView textView) {
        return Double.parseDouble(textView.getText().toString());
    }

}