package com.keralagamestudio.easytrade.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.data.room.Trade;
import com.keralagamestudio.easytrade.data.room.TradeTask;
import com.keralagamestudio.easytrade.ui.addstock.AddStockActivity;
import com.keralagamestudio.easytrade.ui.customview.StocksRecyclerView;
import com.keralagamestudio.easytrade.ui.customview.TodaysPLView;
import com.keralagamestudio.easytrade.utils.Utils;

import java.text.ParseException;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TodaysPLView todaysPLView;
    private StocksRecyclerView stocksRecyclerView;
    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);
        todaysPLView = root.findViewById(R.id.TodaysPL);
        stocksRecyclerView = root.findViewById(R.id.StocksRecyclerView);
        stocksRecyclerView.setActivity(getActivity(), new StocksRecyclerView.StockListener() {
            @Override
            public void onDeleted() {

                todaysPLView.getTodaysData();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBalanceEntered();
            }
        });
      /*  final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/


        return root;
    }

    private void checkBalanceEntered() {
        TradeTask.getTradesByDate(Utils.getCurrentDateAsString(), getContext(), new TradeTask.FetchTradesByDateListener() {
            @Override
            public void onFetched(List<Trade> trades) {
                if (trades == null || trades.isEmpty()){
                    showOpeningBalanceDialog();
                }else {
                    getActivity().startActivity(new Intent(getActivity(), AddStockActivity.class));
                }
            }
        });
    }


    private void showOpeningBalanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_opening_balance,null);
        EditText OpeningBalance = view.findViewById(R.id.OpeningBalance);

        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (OpeningBalance.getText().toString().equals("")){
                            Toast.makeText(getContext(), "Enter valid opening balance", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Trade trade = new Trade();
                        //trade.setDate(Utils.getCurrentDate());
                        try {
                            trade.setDate(Utils.getCurrentDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        trade.setOpeningBalance(OpeningBalance.getText().toString());
                        trade.setPL("");
                        TradeTask.insertTrade(trade,getContext());

                        getActivity().startActivity(new Intent(getActivity(), AddStockActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        todaysPLView.getTodaysData();
        stocksRecyclerView.getStocks();

        todaysPLView.animate().setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        stocksRecyclerView.animate().setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                }).alpha(1);

                        floatingActionButton.animate().setDuration(250)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }
                                }).translationY(1);
                    }
                })
                .alpha(1);
    }

}