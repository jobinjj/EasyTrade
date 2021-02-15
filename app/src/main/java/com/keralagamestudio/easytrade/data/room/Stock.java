package com.keralagamestudio.easytrade.data.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Stock implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int stock_id;

    @ColumnInfo(name = "stock_name")
    private String StockName;

    @ColumnInfo(name = "buy_price")
    private String BuyPrice;

    @ColumnInfo(name = "sell_price")
    private String SellPrice;

    @ColumnInfo(name = "target")
    private String Target;

    @ColumnInfo(name = "stoploss")
    private String StopLoss;

    @ColumnInfo(name = "pl")
    private String PL;

    @ColumnInfo(name = "status")
    private String Status;

    @ColumnInfo(name = "quantity")
    private String Quantity;

    @ColumnInfo(name = "trade_id")
    private int TradeId;

    @TypeConverters({Converters.class})
    private Date date;

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getBuyPrice() {
        return BuyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        BuyPrice = buyPrice;
    }

    public String getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(String sellPrice) {
        SellPrice = sellPrice;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getStopLoss() {
        return StopLoss;
    }

    public void setStopLoss(String stopLoss) {
        StopLoss = stopLoss;
    }

    public String getPL() {
        return PL;
    }

    public void setPL(String PL) {
        this.PL = PL;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public int getTradeId() {
        return TradeId;
    }

    public void setTradeId(int tradeId) {
        TradeId = tradeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
