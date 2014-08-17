package me.voy13k.lpglog;

import java.text.NumberFormat;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.util.TextViewHelper;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TotalsFragment extends Fragment {

    DataStore dataStore;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_totals, container, false);
        rootView.findViewById(R.id.action_change_lpg_conversion_cost).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeLpgConversionCostClicked();
                    }
                });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dataStore = ((Application)activity.getApplication()).getDataStore();
    }

    @Override
    public void onResume() {
        super.onResume();
        TextViewHelper helper = new TextViewHelper(getView());
        float totalSavings = dataStore.getTotalSavings();
        float lpgConversionCost = dataStore.getLpgConversionCost();
        helper.setText(R.id.textTotalSavings, Format.DOLLARS, totalSavings);
        helper.setText(R.id.textLpgConversionCost, Format.DOLLARS, lpgConversionCost);
        helper.setText(R.id.textLpgBalance, Format.DOLLARS, totalSavings - lpgConversionCost);
    }

    private void changeLpgConversionCostClicked() {
        String text = NumberFormat.getInstance().format(dataStore.getLpgConversionCost());
        EditTextDialogFragment.newInstance(R.layout.dialog_lpg_conversion_cost, text).show(
                getActivity().getSupportFragmentManager(), null);
    }

}
