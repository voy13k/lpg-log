package me.voy13k.lpglog;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.util.TextViewHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
        DialogFragment dialogFragment = new DialogFragment() {
            AlertDialog dialog;
            EditText editLpgConversionCost;

            public Dialog onCreateDialog(Bundle savedInstanceState) {
                String text = Format.IN_DOLLARS.format(dataStore.getLpgConversionCost());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_lpg_conversion_cost, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                editLpgConversionCost = (EditText) view.findViewById(android.R.id.input);
                editLpgConversionCost.setText(text);
                builder.setView(view)
                       .setTitle("LPG Conversion cost")
                       .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("dupa", "dupa");
                            }
                       });
                dialog = builder.create();
                return dialog;
            };

            @Override
            public void onResume() {
                super.onResume();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                String costString = editLpgConversionCost.getText().toString();
                                float cost = Float.parseFloat(costString);
                                dataStore.setLpgConversionCost(cost);
                                dialog.dismiss();
                            } catch (NumberFormatException e) {
                                Toast toast = Toast.makeText(getActivity(), "Must be a valid amount", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    });
                editLpgConversionCost.requestFocus();
            }
        };
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

}
