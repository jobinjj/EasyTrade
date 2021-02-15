package com.keralagamestudio.easytrade.data.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface TradeDao {
    @Query("SELECT * FROM Trade")
    List<Trade> getAll();

    @Insert
    void insert(Trade trade);

    @Delete
    void delete(Trade trade);

    @Update
    void update(Trade trade);


    @Query("SELECT * FROM Trade WHERE trade_id == :tradeId")
    Trade getTradeById(int tradeId);

    @Query("SELECT * FROM Trade WHERE date == :date")
    Trade getTradeByDate(Date date);

    @Query("SELECT * FROM Trade ORDER BY :date")
    List<Trade> getTradesByDate(Date date);

    @Query("SELECT * FROM Trade WHERE date BETWEEN :from AND :to")
    List<Trade> findTradesBetweenDates(Date from, Date to);

    @Query("select * from Trade where date=Date(:date)")
    List<Trade> tradesByDate(String date);
}
