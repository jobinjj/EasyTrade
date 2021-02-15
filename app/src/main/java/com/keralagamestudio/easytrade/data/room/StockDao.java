package com.keralagamestudio.easytrade.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StockDao {
    @Query("SELECT * FROM Stock")
    List<Stock> getAll();

    @Insert
    void insert(Stock stock);

    @Delete
    void delete(Stock stock);

    @Update
    void update(Stock stock);

    @Query("SELECT * FROM Stock WHERE date == :date ORDER BY stock_id DESC")
    List<Stock> getStocksByDate(String date);

    @Query("SELECT * FROM Stock WHERE trade_id == :tradeId  ORDER BY stock_id DESC")
    List<Stock> getStocksByTradeId(int tradeId);
}
