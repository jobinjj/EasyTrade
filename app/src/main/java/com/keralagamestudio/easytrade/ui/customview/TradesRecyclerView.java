package com.keralagamestudio.easytrade.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeTask;
import com.keralagamestudio.easytrade.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TradesRecyclerView extends ConstraintLayout {

    private BottomFadingRecyclerView tradesRecycler;
    private ArrayList<Trade> tradesList = new ArrayList<>();
    private TradeRecyclerAdapter tradeRecyclerAdapter;

    public TradesRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public TradesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TradesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_tradesrecycler,this,true);
        tradesRecycler = view.findViewById(R.id.TradesRecycler);
        tradesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        tradeRecyclerAdapter = new TradeRecyclerAdapter(tradesList, getContext(), new TradeClickListener() {
            @Override
            public void onClicked() {
                Toast.makeText(getContext(), "Clicked stock", Toast.LENGTH_SHORT).show();
            }
        });
        tradesRecycler.setAdapter(tradeRecyclerAdapter);
    }

    public void getTrades() {
        tradesList.clear();
        TradeTask.getAllTrades(getContext(), new TradeTask.FetchAllTradesListener() {
            @Override
            public void onFetched(List<Trade> trades) {
                tradesList.addAll(trades);
                tradeRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    public class TradeRecyclerAdapter extends RecyclerView.Adapter<TradeRecyclerAdapter.ItemViewHolder>{
        private List<Trade> tradeList;
        private TradeClickListener tradeClickListener;
        private Context context;

        public TradeRecyclerAdapter(List<Trade> tradeList, Context context, TradeClickListener tradeClickListener) {
            this.tradeList = tradeList;
            this.tradeClickListener = tradeClickListener;
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            final View view = layoutInflater.inflate(R.layout.trades_list_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Trade trade = tradeList.get(position);
            holder.tradeDate.setText(Utils.formatDate(trade.getDate()));
            double PL = Math.round(Double.parseDouble(trade.getPL()) * 100.0 ) / 100.0;
            holder.PL.setText(PL > 0 ? "+" + PL : String.valueOf(PL));
            holder.PL.setTextColor(Double.parseDouble(trade.getPL()) > 0 ? Color.parseColor("#FF00C70A") : Color.parseColor("#FFE60000"));
            holder.Container.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(context, "Opening balance : "+ trade.getOpeningBalance(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return tradeList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tradeDate;
            TextView PL;
            ConstraintLayout Container;
            public ItemViewHolder(final View itemView) {
                super(itemView);
                tradeDate = itemView.findViewById(R.id.TradeDate);
                PL = itemView.findViewById(R.id.PL);
                Container = itemView.findViewById(R.id.Container);
            }
        }

    }

    public interface TradeClickListener{
        void onClicked();
    }
}
