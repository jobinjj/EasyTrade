package com.keralagamestudio.easytrade.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.keralagamestudio.easytrade.R;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinner;
    private String ratio;
    private SharedPreferences.Editor editor;
    private EditText target;
    private EditText editMargin;
    private EditText editLeverage;
    private int leverage;
    private int margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences preferences = getSharedPreferences("EasyTrade",MODE_PRIVATE);
        editor = preferences.edit();
        ratio = preferences.getString("ratio","1:2");
        leverage = preferences.getInt("leverage",5);
        margin = preferences.getInt("margin",1000);
        spinner = findViewById(R.id.spinner);
        target = findViewById(R.id.target);
        editMargin = findViewById(R.id.margin);
        editLeverage = findViewById(R.id.leverage);
        editMargin.setText(String.valueOf(margin));
        editLeverage.setText(String.valueOf(leverage));
        target.setText(String.valueOf(preferences.getFloat("target",0.06f)));

        target.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                editor.putFloat("target",Float.parseFloat(s.toString())).apply();

            }
        });
        editMargin.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                editor.putInt("margin",Integer.parseInt(s.toString())).apply();

            }
        });
        editLeverage.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                    editor.putInt("leverage",Integer.parseInt(s.toString())).apply();

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ratios, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        checkSpinnerSelected(ratio);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerItem = adapterView.getItemAtPosition(i).toString();
                editor.putString("ratio",spinnerItem);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void checkSpinnerSelected(String ratio) {
        switch (ratio){
            case "1:1":
                spinner.setSelection(0);
                break;
            case "1:2":
                spinner.setSelection(1);
                break;
            case "1:3":
                spinner.setSelection(2);
                break;
        }
    }
}