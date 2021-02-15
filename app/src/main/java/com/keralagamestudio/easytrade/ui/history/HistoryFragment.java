package com.keralagamestudio.easytrade.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.keralagamestudio.easytrade.R;
import com.keralagamestudio.easytrade.ui.customview.TradesRecyclerView;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private TradesRecyclerView tradesRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        tradesRecyclerView = root.findViewById(R.id.TradesRecyclerView);
      /*  final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        tradesRecyclerView.getTrades();
    }
}