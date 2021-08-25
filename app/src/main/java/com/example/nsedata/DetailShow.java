package com.example.nsedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;

public class DetailShow extends AppCompatActivity {

    TextView txtSymbolName, txtOpenValue, txtHighValue, txtLowValue, txtPreviousCloseValue,
            txtChangeValue, txtPChangeValue, txtYearHighValue, txtYearLowValue, txtVolumeValue,
            txtValue, txtLastUpdatedTime;

    String number, symbol, open, high, low, pClose, yHigh, yLow, volume, value, lastUpTime;
    String change, pChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_show_activity);

        txtSymbolName = findViewById(R.id.txtSymbolName);
        txtOpenValue = findViewById(R.id.txtOpenValue);
        txtHighValue = findViewById(R.id.txtHighValue);
        txtLowValue = findViewById(R.id.txtLowValue);
        txtPreviousCloseValue = findViewById(R.id.txPreviousCloseValue);
        txtChangeValue = findViewById(R.id.txtChangeValue);
        txtPChangeValue = findViewById(R.id.txtPChangeValue);
        txtYearHighValue = findViewById(R.id.txtYearHighValue);
        txtYearLowValue = findViewById(R.id.txtYearLowValue);
        txtVolumeValue = findViewById(R.id.txtVolumeValue);
        txtValue = findViewById(R.id.txtValue);
        txtLastUpdatedTime = findViewById(R.id.txtLastUpdatedTime);

        Intent ii = getIntent();
        Bundle b = ii.getExtras();
        if (b != null) {
            number = (String) b.get("num");
            symbol = (String) b.get("symbol");
            open = (String) b.get("open");
            high = (String) b.get("high");
            low = (String) b.get("low");
            pClose = (String) b.get("previousClose");
            change = String.valueOf(Float.parseFloat((String) b.get("change")));
            pChange = String.valueOf(Float.parseFloat((String) b.get("pChange")));
            yHigh = (String) b.get("yearHigh");
            yLow = (String) b.get("yearLow");
            volume = (String) b.get("totalTradedVolume");
            value = (String) b.get("totalTradedValue");
            lastUpTime = (String) b.get("lastUpdateTime");

            Objects.requireNonNull(getSupportActionBar()).setTitle(symbol);
        }

        float changee = Float.parseFloat(change);
        float pChangee = Float.parseFloat(pChange);
        if (changee < 0) {
            txtChangeValue.setTextColor(getResources().getColor(R.color.red));
        } else
            txtChangeValue.setTextColor(getResources().getColor(R.color.green));
        if (pChangee < 0) {
            txtPChangeValue.setTextColor(getResources().getColor(R.color.red));
        } else
            txtPChangeValue.setTextColor(getResources().getColor(R.color.green));


        String valuee = (String.valueOf(Double.parseDouble(value)));
        if (Math.abs(Double.parseDouble(value) / 100000) > 1) {
            valuee = String.valueOf((Double.parseDouble(value) / 100000));
           // valuee = formatLakh(Double.parseDouble(valuee));
        }
        Log.e("value", String.valueOf(valuee));



        DecimalFormat formatter = new DecimalFormat("##,##,##0.00");
        DecimalFormat formatter2 = new DecimalFormat("##,##,##0");
        open = formatter.format(Double.parseDouble(open));
        high = formatter.format(Double.parseDouble(high));
        low = formatter.format(Double.parseDouble(low));
        pClose = formatter.format(Double.parseDouble(pClose));
        yHigh = formatter.format(Double.parseDouble(yHigh));
        yLow = formatter.format(Double.parseDouble(yLow));
        volume = formatter2.format(Double.parseDouble(volume));
        value = formatter.format(Double.parseDouble(valuee));
        //volume = String.format("%,d", Long.parseLong(volume.toString()));



        txtSymbolName.setText(symbol);
        txtOpenValue.setText(open);
        txtHighValue.setText(high);
        txtLowValue.setText(low);
        txtPreviousCloseValue.setText(pClose);
        txtChangeValue.setText(change);
        txtPChangeValue.setText(pChange);
        txtYearHighValue.setText(yHigh);
        txtYearLowValue.setText(yLow);
        txtVolumeValue.setText(volume);
        txtValue.setText(value);
        txtLastUpdatedTime.setText(lastUpTime);
    }

/*    private static String formatLakh(double d) {
        String s = String.format(Locale.UK, "%1.2f", Math.abs(d));
        s = s.replaceAll("(.+)(...\\...)", "$1,$2");
        while (s.matches("\\d{3,},.+")) {
            s = s.replaceAll("(\\d+)(\\d{2},.+)", "$1,$2");
        }
        return d < 0 ? ("-" + s) : s;
    }*/

}