package com.keralagamestudio.easytrade.ui.addstock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.keralagamestudio.easytrade.MainActivity;
import com.keralagamestudio.easytrade.R;

public class AddStockActivity extends AppCompatActivity {

    private AddStockViewModel addStockViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trade);

        addStockViewModel = new ViewModelProvider(this).get(AddStockViewModel.class);
    }

    @Override
    public void onBackPressed() {
        if (addStockViewModel.valuesEntered()){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddStockActivity.this);
            builder.setMessage(R.string.dialog_discard_question)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    }).create().show();
        }else super.onBackPressed();

    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addstock_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ratio:

                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}