package me.voy13k.lpglog.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FillUpEntry implements Serializable {


    private static final long serialVersionUID = -3092435627776238421L;

    private Date date;
    private BigDecimal distance;
    private BigDecimal lpgPrice;
    private BigDecimal ulpPrice;
    private BigDecimal lpgVolume;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public BigDecimal getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(BigDecimal lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    public BigDecimal getUlpPrice() {
        return ulpPrice;
    }

    public void setUlpPrice(BigDecimal ulpPrice) {
        this.ulpPrice = ulpPrice;
    }

    public BigDecimal getLpgVolume() {
        return lpgVolume;
    }

    public void setLpgVolume(BigDecimal lpgVolume) {
        this.lpgVolume = lpgVolume;
    }

    @Override
    public String toString() {
        return String.format("{0} {1} {2} {3} {4}", date.getTime(), distance, lpgPrice, ulpPrice, lpgVolume);
    }
}
