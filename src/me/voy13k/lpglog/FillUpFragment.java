package me.voy13k.lpglog;

import me.voy13k.lpglog.data.Dao;
import me.voy13k.lpglog.data.Data;
import me.voy13k.lpglog.data.FillUpEntry;
import me.voy13k.lpglog.widget.DatePickerButton;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FillUpFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fill_up, container, false);
        return rootView;
    }

    public void onDone() {
        FillUpEntry entry = new FillUpEntry();
        entry.setDate(getDatePickerButton(R.id.editDate).getCalendar().getTimeInMillis());
        entry.setDistance(toInt(getDouble(R.id.editDistance) * 1000));
        entry.setLpgPrice(toInt(getDouble(R.id.editLpgPrice) * 10));
        entry.setLpgVolume(toInt(getDouble(R.id.editLpgVolume) * 1000));
        entry.setUlpPrice(toInt(getDouble(R.id.editUnleadedPrice) * 10));
        save(entry);
    }

    private int toInt(Double d) {
        return d.intValue();
    }

    private void save(FillUpEntry entry) {
        Dao.getInstance(getActivity()).save(entry);
        Data.reload();
    }

    private double getDouble(int textViewId) {
        TextView textView = (TextView) getActivity().findViewById(textViewId);
        return Double.parseDouble(textView.getText().toString());
    }

    private DatePickerButton getDatePickerButton(int id) {
        return (DatePickerButton) getActivity().findViewById(id);
    }

}