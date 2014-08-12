package me.voy13k.lpglog.data;

import java.text.MessageFormat;

import android.text.format.DateFormat;

/*
 * So that we can use integers to store data, we use smaller units:
 * distance - m
 * prices - millidollars
 * volume - ml
 * consumption - ml/m (same as l/km)
 */
public class FillUpEntry implements DbContract.FillUp {

    private long id;
    private long date;
    private int distance;
    private int lpgPrice;
    private int ulpPrice;
    private int lpgVolume;
    private int saving;
    private double lpgConsumption;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(int lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    public int getUlpPrice() {
        return ulpPrice;
    }

    public void setUlpPrice(int ulpPrice) {
        this.ulpPrice = ulpPrice;
    }

    public int getLpgVolume() {
        return lpgVolume;
    }

    public void setLpgVolume(int lpgVolume) {
        this.lpgVolume = lpgVolume;
    }

    public void setSaving(int saving) {
        this.saving = saving;
    }

    public int getSaving() {
        return saving;
    }

    public double getLpgConsumption() {
        return lpgConsumption;
    }

    public void setLpgConsumption(double lpgConsumption) {
        this.lpgConsumption = lpgConsumption;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} {1} {2} {3} {4} {5}", id,
                DateFormat.format("yyyy:MM:dd", date), distance, lpgPrice, ulpPrice, lpgVolume);
    }

}
