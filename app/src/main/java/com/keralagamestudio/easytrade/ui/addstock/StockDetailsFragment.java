package com.keralagamestudio.easytrade.ui.addstock;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.data.room.Stock;
import com.keralagamestudio.easytrade.data.room.StockTask;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeTask;
import com.keralagamestudio.easytrade.utils.Utils;

import java.text.ParseException;

import static android.content.Context.MODE_PRIVATE;

public class StockDetailsFragment extends Fragment {


    private EditText stockSymbol;
    private EditText entryPrice;
    private EditText target;
    private EditText stopLoss;
    private EditText quantity;
    private Button btnTarget;
    private Button btnStopLoss;
    private String ratio;
    float targetPercentage;
    float stopLossPercentage;
    int marginAvailable;
    int leverage;
    double quantityValue;

    private Trade trade;
    private AddStockViewModel addStockViewModel;
    private int totalLeverage;
    private TextView breakevenvalue;

    public StockDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addStockViewModel = new ViewModelProvider(requireActivity()).get(AddStockViewModel.class);

        View root = inflater.inflate(R.layout.fragment_stock_details, container, false);
        stockSymbol = root.findViewById(R.id.StockSymbol);
        entryPrice = root.findViewById(R.id.EntryPrice);
        breakevenvalue = root.findViewById(R.id.breakevenvalue);
        target = root.findViewById(R.id.Target);
        stopLoss = root.findViewById(R.id.StopLoss);
        quantity = root.findViewById(R.id.quantity);
        btnTarget = root.findViewById(R.id.BtnTarget);
        btnStopLoss = root.findViewById(R.id.BtnStopLoss);
        addStockViewModel.addEditTexts(entryPrice,stockSymbol,target,stopLoss,quantity);
        SharedPreferences preferences = getActivity().getSharedPreferences("EasyTrade",MODE_PRIVATE);
        ratio = preferences.getString("ratio","1:2");
        targetPercentage = preferences.getFloat("target",0.06f);
        marginAvailable = preferences.getInt("margin",1000);
        leverage = preferences.getInt("leverage",5);
        totalLeverage = marginAvailable * leverage;

        try {
            TradeTask.getTradeByDate(Utils.getCurrentDate(), getContext(), new TradeTask.FetchTradeByDateListener() {
                @Override
                public void onFetched(Trade trad) {
                    trade = trad;
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        entryPrice.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here

                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()){

                    updateTargetAndStopLoss(ratio,s.toString());
                }

            }
        });
        btnTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveStock(true);
            }
        });

        btnStopLoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStock(false);
            }


        });

        return root;
    }

    private void updateTargetAndStopLoss(String ratio,String s) {
        switch (ratio){
            case "1:1":
                stopLossPercentage = targetPercentage;
                break;
            case "1:2":
                stopLossPercentage = targetPercentage / 2;
                break;
            case "1:3":
                stopLossPercentage = targetPercentage / 3;
                break;
        }
        double entryPric = Double.parseDouble(s);
        double targetValue = (entryPric * targetPercentage) / 100;
        double stopLossValue = (entryPric * stopLossPercentage) / 100;
        quantityValue = totalLeverage / Double.parseDouble(s);
        quantity.setText(String.valueOf((int) quantityValue));
        target.setText(String.valueOf(Math.round((entryPric + targetValue)* 100.0 ) / 100.0));
        stopLoss.setText(String.valueOf(Math.round((entryPric - stopLossValue) * 100.0 ) / 100.0));
        double totalCharges = calculateTotalCharges(Double.parseDouble(target.getText().toString()),Double.parseDouble(entryPrice.getText().toString()),(int)quantityValue);
        double breakEvenValue = totalCharges / Double.parseDouble(quantity.getText().toString());
        double finalBreakEvenValue = breakEvenValue + entryPric;
        breakevenvalue.setText(String.valueOf(Math.round(finalBreakEvenValue * 100.0 ) / 100.0));
    }



    private void saveStock(boolean profit) {
        if (stockSymbol.getText().toString().isEmpty()){
            stockSymbol.setError("Enter stock symbol");
            return;
        }
        if (entryPrice.getText().toString().isEmpty()){
            entryPrice.setError("Enter price");
            return;
        }
        if (target.getText().toString().isEmpty()){
            target.setError("Enter target");
            return;
        }
        if (stopLoss.getText().toString().isEmpty()){
            stopLoss.setError("Enter stoploss");
            return;
        }
        if (quantity.getText().toString().isEmpty()){
            quantity.setError("Enter quantity");
            return;
        }
        Stock stock = new Stock();
        stock.setBuyPrice(entryPrice.getText().toString());
        try {
            stock.setDate(Utils.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int quantityValue = Integer.parseInt(quantity.getText().toString());
        stock.setQuantity(quantity.getText().toString());
        stock.setStatus(profit ? "target" : "stoploss");
        stock.setStockName(stockSymbol.getText().toString());
        stock.setTradeId(trade.getTrade_id());
        double targetNum = Double.parseDouble(target.getText().toString());
        double stopLossNum = Double.parseDouble(stopLoss.getText().toString());
        double entryNum = Double.parseDouble(entryPrice.getText().toString());
        double sellPrice = profit ? targetNum : stopLossNum;
        stock.setSellPrice(String.valueOf(sellPrice));
        stock.setStopLoss(stopLoss.getText().toString());
        stock.setTarget(target.getText().toString());
        stock.setPL(calculatePLAfterTax(profit,sellPrice,quantityValue));
        StockTask.insertStocks(stock,getContext());
        updateTodaysPL(stock.getPL());

        getActivity().finish();
    }

    private String calculatePLAfterTax(boolean profit, double sellPrice, int quantityValue) {
        double entryPriceValue = Double.parseDouble(entryPrice.getText().toString());
        double Turnover = (entryPriceValue * quantityValue) + (sellPrice * quantityValue);
        double brokerage =  Math.round(Turnover * 0.03 / 100 * 100.0 ) / 100.0;
        double lowestBrokerage = Turnover * 0.03 / 100;
        double finalBrokerage = Math.round((brokerage >= 40 ? 40 : lowestBrokerage) * 100.0 ) / 100.0;
        double STT = Math.round((sellPrice * quantityValue * 0.025 / 100) * 100.0 ) / 100.0;
        double TransactionCharges = Math.round((Turnover * 0.00345 / 100) * 100.0 ) / 100.0;;
        double GST = Math.round(((finalBrokerage + TransactionCharges) * 18 /100) * 100.0 ) / 100.0;;
        double SEBI = Math.round((Turnover * 0.00005 / 100) * 100.0 ) / 100.0;
        double STAMP = Math.round(((entryPriceValue * quantityValue) * 0.003 / 100) * 100.0 ) / 100.0;
        double totalCharges = Math.round((finalBrokerage + STT + TransactionCharges + GST + SEBI + STAMP) * 100.0 ) / 100.0;
        double targetValue = Double.parseDouble(target.getText().toString());
        double stopLossValue = Double.parseDouble(stopLoss.getText().toString());
        double profitAfterTax = profit ? ((targetValue - entryPriceValue) * quantityValue) - totalCharges : (-(entryPriceValue - stopLossValue) * quantityValue) - totalCharges;
        double profitAfterTaxRounded = Math.round( profitAfterTax * 100.0 ) / 100.0;
        return String.valueOf(profitAfterTaxRounded);
    }

    private Double calculateTotalCharges(double sellPrice, double entryPriceValue, double quantityValue) {
        double Turnover = (entryPriceValue * quantityValue) + (sellPrice * quantityValue);
        double brokerage =  Math.round(Turnover * 0.03 / 100 * 100.0 ) / 100.0;
        double finalBrokerage = Math.round((brokerage > 20 ? 40 : Turnover * 0.03 / 100) * 100.0 ) / 100.0;
        double STT = Math.round((sellPrice * quantityValue * 0.025 / 100) * 100.0 ) / 100.0;
        double TransactionCharges = Math.round((Turnover * 0.00345 / 100) * 100.0 ) / 100.0;;
        double GST = Math.round(((finalBrokerage + TransactionCharges) * 18 /100) * 100.0 ) / 100.0;;
        double SEBI = Math.round((Turnover * 0.00005 / 100) * 100.0 ) / 100.0;
        double STAMP = Math.round(((entryPriceValue * quantityValue) * 0.003 / 100) * 100.0 ) / 100.0;
        double totalCharges = Math.round((finalBrokerage + STT + TransactionCharges + GST + SEBI + STAMP) * 100.0 ) / 100.0;
        return totalCharges;
    }

    private void updateTodaysPL(String pl) {
        try {
            TradeTask.getTradeByDate(Utils.getCurrentDate(), getContext(), new TradeTask.FetchTradeByDateListener() {
                @Override
                public void onFetched(Trade trade) {
                    double todaysPl = Double.parseDouble(trade.getPL().isEmpty() ? "0" : trade.getPL());
                    todaysPl += Double.parseDouble(pl);
                    trade.setPL(String.valueOf(todaysPl));
                    TradeTask.updateTrade(trade,getContext());
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


}