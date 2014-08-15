package me.voy13k.lpglog.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Data {

    private static Data theInstance;

    public static synchronized Data getInstance(Context context) {
        if (theInstance == null) {
            theInstance = new Data(context);
        }
        return theInstance;
    }

    public static synchronized void reload() {
        theInstance = null;
    }

    private List<FillUpEntry> fillUpEntries;

    private float totalDistance;
    private float totalLpgVolume;
    private float totalSavings;

    private boolean loaded;

    private boolean calculated;

    private Data(Context context) {
        fillUpEntries = new ArrayList<FillUpEntry>();
        load(context);
        calculate();
    }

    public List<FillUpEntry> getFillUpEntries() {
        verify(calculated);
        return fillUpEntries;
    }

    public float getAverageLpgConsumption() {
        verify(loaded);
        return 100 * totalLpgVolume / totalDistance;
    }
    
    public float getTotalSavings() {
        verify(calculated);
        return totalSavings;
    }
    
    private void verify(boolean flag) {
        if (!flag) {
            throw new IllegalStateException();
        }
    }

    private void calculate() {
        float averageLpgConsumption = getAverageLpgConsumption();
        float averageUlpConsumption = 10.5f;
        float avgUlpToLpgRatio = averageUlpConsumption / averageLpgConsumption;

        for (FillUpEntry entry : fillUpEntries) {
            float lpgVolume = entry.getLpgVolume();
            entry.setLpgConsumption(100 * lpgVolume / entry.getDistance());
            float lpgCost = lpgVolume * entry.getLpgPrice();
            float ulpCost = lpgVolume * avgUlpToLpgRatio * entry.getUlpPrice();
            float saving = ulpCost - lpgCost;
            entry.setSaving(saving);
            totalSavings += saving;
        }
        calculated = true;
    }

    private void load(Context context) {
        Dao.getInstance(context).loadFillUpEntries(new Dao.OnLoadedListener<FillUpEntry>() {
            @Override
            public void onLoaded(FillUpEntry entry) {
                fillUpEntries.add(entry);
                totalDistance += entry.getDistance();
                totalLpgVolume += entry.getLpgVolume();
            }
        });
        loaded = true;
    }

}
