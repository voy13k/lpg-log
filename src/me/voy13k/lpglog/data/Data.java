package me.voy13k.lpglog.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.util.LogPrinter;

public class Data {

    private static final LogPrinter INFO = new LogPrinter(Log.INFO,
            Data.class.getCanonicalName());

    private static Data theInstance;

    public static synchronized Data getInstance(Context context) {
        if (theInstance == null) {
            theInstance = new Data(context);
        }
        return theInstance;
    }

    public static synchronized void reload() {
        theInstance = null;
        INFO.println("reloaded");
    }

    private List<FillUpEntry> fillUpEntries;

    private double totalDistance; // m
    private double totalLpgVolume; // ml

    private Data(Context context) {
        fillUpEntries = new ArrayList<FillUpEntry>();
        load(context);
        recalculate();
    }

    public List<FillUpEntry> getFillUpEntries() {
        return fillUpEntries;
    }

    private void recalculate() {
        double averageLpgConsumption = totalLpgVolume / totalDistance;
        double averageUlpConsumption = 10.5d / 100d;
        double ulpToLpgRatio = averageUlpConsumption / averageLpgConsumption;

        for (FillUpEntry entry : fillUpEntries) {
            double lpgVolume = entry.getLpgVolume();
            double lpgCost = 0.001 * lpgVolume * entry.getLpgPrice();
            double ulpCost = 0.001 * lpgVolume * ulpToLpgRatio * entry.getUlpPrice();
            entry.setSaving(toInt(ulpCost - lpgCost));
            entry.setLpgConsumption(lpgVolume / entry.getDistance());
        }
    }

    private int toInt(Double d) {
        return d.intValue();
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
    }

}
