package com.keralagamestudio.easytrade.ui.addstock;

import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

public class AddStockViewModel extends ViewModel {
    String testMessage;
    EditText entryPrice,stockName,target,stopLoss,quantity;
    public AddStockViewModel() {

    }

    public void addEditTexts(EditText entryPrice, EditText stockName, EditText target, EditText stopLoss, EditText quantity) {
        this.entryPrice = entryPrice;
        this.stockName = stockName;
        this.target = target;
        this.stopLoss = stopLoss;
        this.quantity = quantity;
    }

    public boolean valuesEntered(){
        return !entryPrice.getText().toString().isEmpty() || !stockName.getText().toString().isEmpty() || !target.getText().toString().isEmpty() || !stopLoss.getText().toString().isEmpty()  || !quantity.getText().toString().isEmpty();
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }

    public String getTestMessage() {
        return testMessage;
    }



}
