package me.voy13k.lpglog;


import me.voy13k.lpglog.data.Data;
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
	    Data data = Data.getInstance(getActivity());
        setTotalSavings(data);
        setAverageLpgConsumption(data);
	}

    private void setTotalSavings(Data data) {
        float totalSavings = data.getTotalSavings();
        String totalSavingsTxt = Format.DOLLARS.format(totalSavings);
        ((TextView)getActivity().findViewById(R.id.textTotalSavings)).setText(totalSavingsTxt);
    }

    private void setAverageLpgConsumption(Data data) {
        float avgLpgConsumption = data.getAverageLpgConsumption();
        String totalAvgLpgConsumptionTxt = Format.CONSUMPTION.format(avgLpgConsumption);
        ((TextView)getActivity().findViewById(R.id.textAvgLpgConsumption)).setText(totalAvgLpgConsumptionTxt);
    }
}
