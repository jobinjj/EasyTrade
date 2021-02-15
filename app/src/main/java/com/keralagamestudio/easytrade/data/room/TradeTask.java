package com.keralagamestudio.easytrade.data.room;

import android.content.Context;
import android.os.AsyncTask;

import com.keralagamestudio.easytrade.utils.MyApplication;

import java.util.Date;
import java.util.List;

public class TradeTask {

    private static Trade trade;

    public static void getTradeById(int trade_id){
       new GetTradeByIdTask().execute(trade_id);
    }
    public static void getTradeByDate(Date date, Context context, FetchTradeByDateListener fetchTradeByDateListener){
       new GetTradeByDateTask(fetchTradeByDateListener,context).execute(date);
    }
    public static void getTradesByDate(String date, Context context,FetchTradesByDateListener fetchTradesByDateListener){
       new GetTradesByDateTask(fetchTradesByDateListener,context).execute(date);
    }
    public static void insertTrade(Trade trade,Context context){
       new InsertTrade(context).execute(trade);
    }
    public static void updateTrade(Trade trade,Context context){
       new UpdateTrade(context).execute(trade);
    }
    public static void getAllTrades(Context context,FetchAllTradesListener fetchAllTradesListener){
       new GetAllTrades(context,fetchAllTradesListener).execute();
    }
    static class GetTradeByIdTask extends AsyncTask<Integer,Void, Trade>{

        @Override
        protected Trade doInBackground(Integer... ids) {
            Trade trade = DatabaseClient.getInstance(new MyApplication().getContext())
                    .getAppDatabase()
                    .tradesDao()
                    .getTradeById(ids[0]);
            return trade;
        }

        @Override
        protected void onPostExecute(Trade trade) {

            super.onPostExecute(trade);
        }
    }
    static class GetTradeByDateTask extends AsyncTask<Date,Void, Trade>{

        FetchTradeByDateListener fetchTradeByDateListener;
        Context context;

        public GetTradeByDateTask(FetchTradeByDateListener fetchTradeByDateListener, Context context) {
            this.fetchTradeByDateListener = fetchTradeByDateListener;
            this.context = context;
        }

        @Override
        protected Trade doInBackground(Date... dates) {
            try {
                trade = DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .tradesDao()
                        .getTradeByDate(dates[0]);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return trade;
        }

        @Override
        protected void onPostExecute(Trade trade) {
            super.onPostExecute(trade);
            fetchTradeByDateListener.onFetched(trade);
        }
    }
    static class GetTradesByDateTask extends AsyncTask<String,Void, List<Trade>>{

        FetchTradesByDateListener fetchTradesByDateListener;
        Context context;

        public GetTradesByDateTask(FetchTradesByDateListener fetchTradesByDateListener, Context context) {
            this.fetchTradesByDateListener = fetchTradesByDateListener;
            this.context = context;
        }

        @Override
        protected List<Trade> doInBackground(String... dates) {
            List<Trade> trades = DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .tradesDao()
                    .tradesByDate(dates[0]);
            return trades;
        }

        @Override
        protected void onPostExecute(List<Trade> trades) {
            super.onPostExecute(trades);
            fetchTradesByDateListener.onFetched(trades);
        }
    }
    static class GetAllTrades extends AsyncTask<String,Void, List<Trade>>{

        FetchAllTradesListener fetchAllTradesListener;
        Context context;

        public GetAllTrades(Context context,FetchAllTradesListener fetchAllTradesListener) {
            this.fetchAllTradesListener = fetchAllTradesListener;
            this.context = context;
        }

        @Override
        protected List<Trade> doInBackground(String... dates) {
            List<Trade> trades = DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .tradesDao()
                    .getAll();
            return trades;
        }

        @Override
        protected void onPostExecute(List<Trade> trades) {
            super.onPostExecute(trades);
            fetchAllTradesListener.onFetched(trades);
        }
    }
    static class InsertTrade extends AsyncTask<Trade,Void, Trade>{

        Context context;

        public InsertTrade(Context context) {
            this.context = context;
        }

        @Override
        protected Trade doInBackground(Trade... trades) {
            try {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .tradesDao()
                        .insert(trades[0]);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return null;
        }
    }
    static class UpdateTrade extends AsyncTask<Trade,Void, Trade>{

        Context context;

        public UpdateTrade(Context context) {
            this.context = context;
        }

        @Override
        protected Trade doInBackground(Trade... trades) {
            DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .tradesDao()
                    .update(trades[0]);
            return null;
        }
    }

    public interface FetchTradeByDateListener {
        void onFetched(Trade trade);
    }
    public interface FetchTradesByDateListener {
        void onFetched(List<Trade> trades);
    }
    public interface FetchAllTradesListener {
        void onFetched(List<Trade> trades);
    }
}
