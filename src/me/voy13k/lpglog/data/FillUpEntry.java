package me.voy13k.lpglog.data;

import java.text.MessageFormat;

import android.text.format.DateFormat;

/*
 * So that we can use integers to store data, we use smaller units:
 * distance - km
 * prices - dollars
 * volume - ltr
 * consumption - ltr/km (not ltr/100km !!)
 */
public class FillUpEntry implements DbContract.FillUp {

    private long id;
    private long date;
    private float distance;
    private float lpgPrice;
    private float ulpPrice;
    private float lpgVolume;
    private float saving;
    private float lpgConsumption;

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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(float lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    public float getUlpPrice() {
        return ulpPrice;
    }

    public void setUlpPrice(float ulpPrice) {
        this.ulpPrice = ulpPrice;
    }

    public float getLpgVolume() {
        return lpgVolume;
    }

    public void setLpgVolume(float lpgVolume) {
        this.lpgVolume = lpgVolume;
    }

    public float getSaving() {
        return saving;
    }

    public void setSaving(float saving) {
        this.saving = saving;
    }
    
    public float getLpgConsumption() {
        return lpgConsumption;
    }

    public void setLpgConsumption(float lpgConsumption) {
        this.lpgConsumption = lpgConsumption;
    }

    public String toString() {
        return MessageFormat.format("{0} {1} {2} {3} {4} {5}", id,
                DateFormat.format("yyyy:MM:dd", date), distance, lpgPrice, ulpPrice, lpgVolume);
    }

}
