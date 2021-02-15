package com.keralagamestudio.easytrade.ui.addstock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keralagamestudio.easytrade.R;

public class StockConfirmationFragment extends Fragment {


    public StockConfirmationFragment() {
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
        AddStockViewModel addStockViewModel = new ViewModelProvider(requireActivity()).get(AddStockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stock_confirmation, container, false);
        root.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_stockConfirmationFragment_to_stockDetailsFragment);
            }
        });
        return root;
    }
}