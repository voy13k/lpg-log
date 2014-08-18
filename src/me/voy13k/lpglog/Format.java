package me.voy13k.lpglog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public interface Format {

    java.text.Format CENTS = new DecimalFormat("0.0#");
    java.text.Format CONSUMPTION = new DecimalFormat("0.0#");
    java.text.Format DATE = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
    java.text.Format DISTANCE = new DecimalFormat("0.0#");
    java.text.Format DOLLARS = NumberFormat.getCurrencyInstance();
    java.text.Format VOLUME = new DecimalFormat("0.0#");
    java.text.Format IN_DOLLARS = new DecimalFormat("0.##");

}
