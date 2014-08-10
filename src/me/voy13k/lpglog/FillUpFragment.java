package me.voy13k.lpglog;

import java.math.BigDecimal;

import me.voy13k.lpglog.data.FillUpEntry;
import me.voy13k.lpglog.widget.DatePickerButton;
import android.app.Activity;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class FillUpFragment extends Fragment {

    public interface Container {
        void setFillUpFragment(FillUpFragment fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fill_up, container, false);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((Container) activity).setFillUpFragment(this);
    }

    @Override
    public void onDetach() {
        ((Container) getActivity()).setFillUpFragment(null);
        super.onDetach();
    }

    public void onDone() {
        FillUpEntry entry = new FillUpEntry();
        entry.setDate(getDatePickerButton(R.id.editDate).getCalendar().getTime());
        entry.setDistance(getBigDecimal(R.id.editDistance));
        entry.setLpgPrice(getBigDecimal(R.id.editLpgPrice));
        entry.setLpgVolume(getBigDecimal(R.id.editLpgVolume));
        entry.setUlpPrice(getBigDecimal(R.id.editUnleadedPrice));
        save(entry);
    }

    private void save(FillUpEntry entry) {
        Log.i(FillUpFragment.class.getCanonicalName(), entry.toString());
    }

    private BigDecimal getBigDecimal(int id) {
        TextView textView = (TextView)getActivity().findViewById(id);
        return new BigDecimal(textView.getText().toString());
    }

    private DatePickerButton getDatePickerButton(int id) {
        return (DatePickerButton)getActivity().findViewById(id);
    }

}