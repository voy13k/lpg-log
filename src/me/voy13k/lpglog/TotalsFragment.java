package me.voy13k.lpglog;


import me.voy13k.lpglog.data.DataStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TotalsFragment extends Fragment {

    @Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_totals, container,
				false);
		return rootView;
	}

	@Override
	public void onResume() {
	    super.onResume();
	    DataStore dataStore = ((Application)getActivity().getApplication()).getDataStore();
        setTotalSavings(dataStore.getTotalSavings());
        setAverageLpgConsumption(dataStore.getAverageLpgConsumption());
	}

    private void setTotalSavings(float totalSavings) {
        String totalSavingsTxt = Format.DOLLARS.format(totalSavings);
        ((TextView)getActivity().findViewById(R.id.textTotalSavings)).setText(totalSavingsTxt);
    }

    private void setAverageLpgConsumption(float avgLpgConsumption) {
        String totalAvgLpgConsumptionTxt = Format.CONSUMPTION.format(avgLpgConsumption);
        ((TextView)getActivity().findViewById(R.id.textAvgLpgConsumption)).setText(totalAvgLpgConsumptionTxt);
    }
}
