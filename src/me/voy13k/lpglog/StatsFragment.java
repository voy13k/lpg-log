package me.voy13k.lpglog;

import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.data.DataStore.OnDataChangedListener;
import me.voy13k.lpglog.util.TextViewHelper;
import me.voy13k.lpglog.widget.EditTextDialogFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class StatsFragment extends Fragment implements OnDataChangedListener {

    DataStore dataStore;

    @Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_stats, container,
				false);
        rootView.findViewById(R.id.action_change_avg_ulp_consumption).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeAvgUlpConsumptionClicked();
                    }
                });
		return rootView;
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dataStore = ((Application) activity.getApplication()).getDataStore();
        this.dataStore.register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dataStore.deregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.dataStore.deregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onDataChanged() {
        refresh();
    }

    private void refresh() {
        TextViewHelper helper = new TextViewHelper(getView());
        float avgLpgConsumption = dataStore.getAverageLpgConsumption();
        float avgUlpConsumption = dataStore.getAverageUlpConsumption();
        helper.setText(R.id.textAvgLpgConsumption, Format.CONSUMPTION, avgLpgConsumption);
        helper.setText(R.id.textAvgUlpConsumption, Format.CONSUMPTION, avgUlpConsumption);
    }
    
    private void changeAvgUlpConsumptionClicked() {
        UlpConsumptionDialog dialogFragment = new UlpConsumptionDialog();
        String text = Format.IN_DOLLARS.format(dataStore.getAverageUlpConsumption());
        UlpConsumptionDialog.setArguments(dialogFragment, R.layout.dialog_avg_ulp_consumption,
                "Avg ULP Consumption", text, "Set");
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    public static class UlpConsumptionDialog extends EditTextDialogFragment {
        @Override
        protected boolean onPositiveButtonClick(String text) {
            try {
                DataStore dataStore = ((Application) getActivity().getApplication())
                        .getDataStore();
                dataStore.setAverageUlpConsumption(Float.parseFloat(text));
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
