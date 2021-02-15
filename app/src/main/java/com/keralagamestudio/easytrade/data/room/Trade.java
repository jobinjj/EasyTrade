package com.keralagamestudio.easytrade.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Trade implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int trade_id;


    @ColumnInfo(name = "date")
    @TypeConverters({Converters.class})
    public Date date;

    @ColumnInfo(name = "pl")
    private String PL;

    @ColumnInfo(name = "opening_balance")
    private String OpeningBalance;

    public int getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(int trade_id) {
        this.trade_id = trade_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPL() {
        return PL;
    }

    public void setPL(String PL) {
        this.PL = PL;
    }

    public String getOpeningBalance() {
        return OpeningBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        OpeningBalance = openingBalance;
    }

}
