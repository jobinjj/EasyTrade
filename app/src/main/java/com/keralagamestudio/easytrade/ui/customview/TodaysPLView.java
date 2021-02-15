package com.keralagamestudio.easytrade.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeTask;
import com.keralagamestudio.easytrade.utils.Utils;

import java.text.ParseException;

public class TodaysPLView extends ConstraintLayout {

    private TextView txtPL;
    private TextView openingBalance;
    private Trade trade1;

    public TodaysPLView(@NonNull Context context) {
        super(context);
        init();
    }

    public TodaysPLView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TodaysPLView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_todays_pl,this,true);
        txtPL = view.findViewById(R.id.txt_pl);
        openingBalance = view.findViewById(R.id.OpeningBalance);
    }

    public void getTodaysData() {
        try {
            TradeTask.getTradeByDate(Utils.getCurrentDate(), getContext(), new TradeTask.FetchTradeByDateListener() {
                @Override
                public void onFetched(Trade trade) {
                    if (trade != null){
                        trade1 = trade;
                        if (!trade.getPL().equals("")){
                            double PL = Math.round(Double.parseDouble(trade.getPL()) * 100.0 ) / 100.0;
                            txtPL.setText(PL > 0 ? "+" + String.valueOf(PL) : String.valueOf(PL));
                            txtPL.setTextColor(Double.parseDouble(trade.getPL()) > 0 ? Color.parseColor("#FF00C70A") : Color.parseColor("#FFE60000"));
                        }else txtPL.setText("0");
                        if (!trade.getOpeningBalance().equals("")){
                            openingBalance.setText(trade.getOpeningBalance());
                        }else openingBalance.setText("0");
                    }
                    else{
                        txtPL.setText("0");
                        openingBalance.setText("0");
                    }
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
