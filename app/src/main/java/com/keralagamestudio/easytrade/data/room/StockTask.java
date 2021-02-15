package com.keralagamestudio.easytrade.data.room;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class StockTask {

    public static void getStocksByDate(String date, Context context,FetchStocksByDateListener fetchStocksByDateListener){
        new GetStocksByDateTask(fetchStocksByDateListener, context).execute(date);
    }

    public static void getStocksByTradeId(Integer tradeId, Context context,FetchStocksByTradeIdListener fetchStocksByTradeIdListener){
        new GetStocksByTradeIdTask(fetchStocksByTradeIdListener, context).execute(tradeId);
    }

    public static void insertStocks(Stock stock,Context context){
        new InsertStock(context).execute(stock);
    }

    public static void deleteStock(Stock stock,Context context,DeleteStockListener listener){
        new DeleteStock(context,listener).execute(stock);
    }

    static class GetStocksByDateTask extends AsyncTask<String,Void, List<Stock>> {

        FetchStocksByDateListener fetchStocksByDateListener;
        Context context;

        public GetStocksByDateTask(FetchStocksByDateListener fetchStocksByDateListener, Context context) {
            this.fetchStocksByDateListener = fetchStocksByDateListener;
            this.context = context;
        }

        @Override
        protected List<Stock> doInBackground(String... dates) {
            List<Stock> stockList = DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .stocksDao()
                    .getStocksByDate(dates[0]);
            return stockList;
        }

        @Override
        protected void onPostExecute(List<Stock> stock) {
            super.onPostExecute(stock);
            fetchStocksByDateListener.onFetched(stock);
        }
    }

    static class GetStocksByTradeIdTask extends AsyncTask<Integer,Void, List<Stock>> {

        FetchStocksByTradeIdListener fetchStocksByTradeIdListener;
        Context context;

        public GetStocksByTradeIdTask(FetchStocksByTradeIdListener fetchStocksByTradeIdListener, Context context) {
            this.fetchStocksByTradeIdListener = fetchStocksByTradeIdListener;
            this.context = context;
        }

        @Override
        protected List<Stock> doInBackground(Integer... ids) {
            List<Stock> stockList = DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .stocksDao()
                    .getStocksByTradeId(ids[0]);
            return stockList;
        }

        @Override
        protected void onPostExecute(List<Stock> stockList) {
            super.onPostExecute(stockList);
            fetchStocksByTradeIdListener.onFetched(stockList);
        }
    }

    static class InsertStock extends AsyncTask<Stock,Void, Void>{

        Context context;

        public InsertStock(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Stock... stocks) {
            DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .stocksDao()
                    .insert(stocks[0]);
            return null;
        }
    }

    static class DeleteStock extends AsyncTask<Stock,Void, Void>{

        Context context;
        private DeleteStockListener listener;

        public DeleteStock(Context context, DeleteStockListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Stock... stocks) {
            DatabaseClient.getInstance(context)
                    .getAppDatabase()
                    .stocksDao()
                    .delete(stocks[0]);

            listener.onDeleted(stocks[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public interface FetchStocksByDateListener{
        void onFetched(List<Stock> stocksList);
    }
    public interface FetchStocksByTradeIdListener{
        void onFetched(List<Stock> stocksList);
    }
    public interface DeleteStockListener{
        void onDeleted(Stock stock);
    }
}
