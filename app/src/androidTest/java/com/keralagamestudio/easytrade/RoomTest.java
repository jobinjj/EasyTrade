package com.keralagamestudio.easytrade;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.keralagamestudio.easytrade.data.room.AppDatabase1;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeDao;
import com.keralagamestudio.easytrade.utils.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RoomTest {
    private TradeDao tradesDao;
    private AppDatabase1 db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase1.class).build();
        tradesDao = db.tradesDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {

        for (int i = 0; i < 5; i++) {
            Calendar c = Calendar.getInstance();
            Trade trade = new Trade();
            trade.setPL("0");
            trade.setDate(Utils.getCurrentDate());
            trade.setOpeningBalance("0");
            tradesDao.insert(trade);
        }

        for (int i = 0; i < 4; i++) {
            Trade trade = new Trade();
            trade.setPL("0");
            trade.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-06"));
            trade.setOpeningBalance("0");
            tradesDao.insert(trade);
        }


        List<Trade> trades = tradesDao.getAll();
        List<Trade> tradesByDateBetween = tradesDao.findTradesBetweenDates(Utils.getFirstDayOfMonth(),Utils.getLastDayOfMonth());
        List<Trade> tradeByDate = tradesDao.tradesByDate(Utils.getCurrentDateAsString());
    }
}