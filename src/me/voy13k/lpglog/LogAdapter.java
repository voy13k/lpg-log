package me.voy13k.lpglog;

import java.util.Collection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import me.voy13k.lpglog.data.DataStore;
import me.voy13k.lpglog.data.DataStore.OnDataChangedListener;
import me.voy13k.lpglog.data.FillUpEntry;
import me.voy13k.lpglog.util.TextViewHelper;

public class LogAdapter extends ArrayAdapter<FillUpEntry> implements OnDataChangedListener {

    private LayoutInflater layoutInflater;
    private DataStore dataStore;

    public LogAdapter(Activity activity) {
        super(activity, 0);
        this.layoutInflater = activity.getLayoutInflater();
        dataStore = ((Application) activity.getApplication()).getDataStore();
        onDataChanged();
        dataStore.register(this);
    }

    public void addAll(Collection<? extends FillUpEntry> entries) {
        for (FillUpEntry entry : entries) {
            add(entry);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_log, parent, false);
            fill(convertView, position);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private void fill(View itemView, int position) {
        FillUpEntry entry = getItem(position);
        TextViewHelper helper = new TextViewHelper(itemView);
        helper.setText(R.id.date, Format.DATE, entry.getDate());
        helper.setText(R.id.gasConsupmtion, Format.CONSUMPTION, entry.getLpgConsumption());
        helper.setText(R.id.saving, Format.DOLLARS, entry.getSaving());
    }

    @Override
    public void onDataChanged() {
        clear();
        addAll(dataStore.getFillUpEntries());
        notifyDataSetChanged();
    }

    public void onDestroyView() {
        dataStore.deregister(this);
    }
}
