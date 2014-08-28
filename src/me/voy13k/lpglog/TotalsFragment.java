package me.voy13k.lpglog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.util.TextViewHelper;
import me.voy13k.lpglog.widget.EditTextDialogFragment;

public class TotalsFragment extends Fragment implements DataStore.OnDataChangedListener {

    private DataStore dataStore;

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
        this.dataStore = ((Application) activity.getApplication()).getDataStore();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.dataStore.deregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.dataStore.register(this);
        refresh();
    }

    @Override
    public void onDataChanged() {
        refresh();
    }

    private void refresh() {
        TextViewHelper helper = new TextViewHelper(getView());
        float totalSavings = dataStore.getTotalSavings();
        float lpgConversionCost = dataStore.getLpgConversionCost();
        helper.setText(R.id.textTotalSavings, Format.DOLLARS, totalSavings);
        helper.setText(R.id.textLpgConversionCost, Format.DOLLARS, lpgConversionCost);
        helper.setText(R.id.textLpgBalance, Format.DOLLARS, totalSavings - lpgConversionCost);
    }

    private void changeLpgConversionCostClicked() {
        LpgConversionDialog dialogFragment = new LpgConversionDialog();
        String text = Format.IN_DOLLARS.format(dataStore.getLpgConversionCost());
        LpgConversionDialog.setArguments(dialogFragment, R.layout.dialog_lpg_conversion_cost,
                "LPG Conversion cost", text, "Set");
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    public static class LpgConversionDialog extends EditTextDialogFragment {
        @Override
        protected boolean onPositiveButtonClick(String text) {
            try {
                DataStore dataStore = ((Application) getActivity().getApplication())
                        .getDataStore();
                dataStore.setLpgConversionCost(Float.parseFloat(text));
                return true;
            } catch (NumberFormatException e) {
                Toast toast = Toast.makeText(getActivity(), "Must be a valid amount",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
        }
    }
}
