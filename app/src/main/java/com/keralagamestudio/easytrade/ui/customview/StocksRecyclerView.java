package com.keralagamestudio.easytrade.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.data.room.Stock;
import com.keralagamestudio.easytrade.data.room.StockTask;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeTask;
import com.keralagamestudio.easytrade.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StocksRecyclerView extends ConstraintLayout {

    private BottomFadingRecyclerView stocksRecycler;
    private ArrayList<Stock> stockList = new ArrayList<>();
    private StockRecyclerAdapter stockRecyclerAdapter;
    private Activity activity;
    private StockListener stockListener;

    public StocksRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public StocksRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StocksRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_stocksrecycler,this,true);
        stocksRecycler = view.findViewById(R.id.StocksRecycler);
        stocksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void setActivity(Activity activity,StockListener stockListener) {
        this.stockListener = stockListener;
        this.activity = activity;
        stockRecyclerAdapter = new StockRecyclerAdapter(stockList, activity, new StockTask.DeleteStockListener() {
            @Override
            public void onDeleted(Stock stock) {
                 getStocks();
                try {
                    TradeTask.getTradeByDate(Utils.getCurrentDate(), getContext(), new TradeTask.FetchTradeByDateListener() {
                        @Override
                        public void onFetched(Trade trade) {
                            double todaysPl = Double.parseDouble(trade.getPL().isEmpty() ? "0" : trade.getPL());
                            todaysPl -= Double.parseDouble(stock.getPL());
                            trade.setPL(String.valueOf(todaysPl));
                            TradeTask.updateTrade(trade,getContext());

                            stockListener.onDeleted();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        stocksRecycler.setAdapter(stockRecyclerAdapter);
    }

    public void getStocks() {
        stockList.clear();
        try {
            TradeTask.getTradeByDate(Utils.getCurrentDate(), getContext(), new TradeTask.FetchTradeByDateListener() {
                @Override
                public void onFetched(Trade trade) {
                    if (trade!=null){
                        StockTask.getStocksByTradeId(trade.getTrade_id(), getContext(), new StockTask.FetchStocksByTradeIdListener() {
                            @Override
                            public void onFetched(List<Stock> stocksList) {
                                stockList.addAll(stocksList);
                                stockRecyclerAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ItemViewHolder>{
        private List<Stock> stockList;
        private Activity activity;
        private StockTask.DeleteStockListener deleteStockListener;

        public StockRecyclerAdapter(List<Stock> stockList, Activity activity, StockTask.DeleteStockListener deleteStockListener) {
            this.stockList = stockList;
            this.activity = activity;
            this.deleteStockListener = deleteStockListener;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final LayoutInflater layoutInflater = LayoutInflater.from(activity);
            final View view = layoutInflater.inflate(R.layout.stocks_list_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

            Stock stock = stockList.get(position);
            holder.stockName.setText(stock.getStockName());
            holder.PL.setText(Double.parseDouble(stock.getPL()) > 0 ? "+" + stock.getPL() : stock.getPL());
            holder.quantity.setText("x" + stock.getQuantity());
            holder.PL.setTextColor(Double.parseDouble(stock.getPL()) > 0 ? Color.parseColor("#FF00C70A") : Color.parseColor("#FFE60000"));
            holder.Container.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    StockTask.deleteStock(stock, activity, new StockTask.DeleteStockListener() {
                        @Override
                        public void onDeleted(Stock stock1) {
                            Thread thread = new Thread(){
                                public void run(){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            deleteStockListener.onDeleted(stock);
                                        }
                                    });
                                }
                            };
                            thread.start();
                        }
                    });
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return stockList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView stockName;
            TextView PL;
            TextView quantity;
            ConstraintLayout Container;
            public ItemViewHolder(final View itemView) {
                super(itemView);
                stockName = itemView.findViewById(R.id.TradeDate);
                PL = itemView.findViewById(R.id.PL);
                quantity = itemView.findViewById(R.id.quantity);
                Container = itemView.findViewById(R.id.Container);
            }
        }

    }

    public interface StockListener{
        void onDeleted();
    }
}
