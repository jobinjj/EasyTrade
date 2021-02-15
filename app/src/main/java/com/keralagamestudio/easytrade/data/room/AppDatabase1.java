package com.keralagamestudio.easytrade.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Trade.class, Stock.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase1 extends RoomDatabase {
    public abstract TradeDao tradesDao();
    public abstract StockDao stocksDao();
}
