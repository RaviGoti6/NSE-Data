package com.example.nsedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    //ListAdapter adptr;
    ListAdapter adapter;

    //String symbol, open, high, low;

    ArrayList<HashMap<String, String>> leadList;

    public ArrayList<HashMap<String, String>> orig;

    OkHttpClient client = new OkHttpClient();

    TextView txtNumber;

    public String url = "https://nse-data1.p.rapidapi.com/nifty_fifty_indices_data";
    //public String url = "https://reqres.in/api/users?page=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);

        leadList = new ArrayList<>();

        //txtSymbol = (TextView) findViewById(R.id.txtSymbol);
        txtNumber = (TextView) findViewById(R.id.txtNumber);


        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) listView.getAdapter().getItem(position);
                String num = (String) obj.get("number");
                String symbol = (String) obj.get("symbol");
                String open = (String) obj.get("open");
                String high = (String) obj.get("dayHigh");
                String low = (String) obj.get("dayLow");
                String lastPrice = (String) obj.get("lastPrice");
                String previousClose = (String) obj.get("previousClose");
                String change = (String) obj.get("change");
                String pChange = (String) obj.get("pChange");
                String yearHigh = (String) obj.get("yearHigh");
                String yearLow = (String) obj.get("yearLow");
                String totalTradedVolume = (String) obj.get("totalTradedVolume");
                String totalTradedValue = (String) obj.get("totalTradedValue");
                String lastUpdateTime = (String) obj.get("lastUpdateTime");

                Toast.makeText(MainActivity.this, "Number:" + num, Toast.LENGTH_SHORT).show();
                Log.e("num=" , num);

                Intent i = new Intent(MainActivity.this, DetailShow.class);
                i.putExtra("num", num);
                i.putExtra("symbol", symbol);
                i.putExtra("open", open);
                i.putExtra("high", high);
                i.putExtra("low", low);
                i.putExtra("lastPrice", lastPrice);
                i.putExtra("previousClose", previousClose);
                i.putExtra("change", change);
                i.putExtra("pChange", pChange);
                i.putExtra("yearHigh", yearHigh);
                i.putExtra("yearLow", yearLow);
                i.putExtra("totalTradedVolume", totalTradedVolume);
                i.putExtra("totalTradedValue", totalTradedValue);
                i.putExtra("lastUpdateTime", lastUpdateTime);

                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to Search");
        searchView.setIconifiedByDefault(false);
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {
                    listView.clearTextFilter();
                } else {
                    listView.setFilterText(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
                .addHeader("x-rapidapi-host", BuildConfig.API_HOST)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                //response = client.newCall(request).execute();

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject jsonObj = new JSONObject(myResponse);
                            Log.e("Test jsonObj=", jsonObj.toString());

                            JSONArray jsonData = jsonObj.getJSONObject("body").getJSONArray("data");
                            Log.e("Test :", String.valueOf(jsonData));

                            for (int i = 0; i < jsonData.length(); i++) {

                                JSONObject c = jsonData.getJSONObject(i);

                                String status = jsonObj.getString("status");
                                String data = jsonObj.getJSONObject("body").getString("data");

                                //Log.e("Test page=", status);
                                //Log.e("Test data=", data);

                                String symbol = c.getString("symbol");
                                String open = c.getString("open");
                                String high = c.getString("dayHigh");
                                String low = c.getString("dayLow");
                                String lastPrice = c.getString("lastPrice");
                                String previousClose = c.getString("previousClose");
                                String change = String.valueOf(Float.parseFloat(c.getString("change")));
                                String pChange = String.valueOf(Float.parseFloat(c.getString("pChange")));
                                String yearHigh = c.getString("yearHigh");
                                String yearLow = c.getString("yearLow");
                                String totalTradedVolume = c.getString("totalTradedVolume");
                                String totalTradedValue = c.getString("totalTradedValue");
                                String lastUpdateTime = c.getString("lastUpdateTime");

                                HashMap<String, String> map = new HashMap<>();
                                int pos = Integer.parseInt(String.valueOf((listView.getCount() + 1)));
                                String num;
                                if (pos <= 9) {
                                    num = String.format("%3d", pos) + ".";
                                    //num = num +".";
                                } else {
                                    num = pos + ".";
                                }
                                map.put("symbol", symbol);
                                map.put("open", open);
                                map.put("dayHigh", high);
                                map.put("dayLow", low);
                                map.put("lastPrice", lastPrice);
                                map.put("previousClose", previousClose);
                                map.put("change", change);
                                map.put("pChange", pChange);
                                map.put("yearHigh", yearHigh);
                                map.put("yearLow", yearLow);
                                map.put("totalTradedVolume", totalTradedVolume);
                                map.put("totalTradedValue", totalTradedValue);
                                map.put("lastUpdateTime", lastUpdateTime);
                                map.put("number", num);

                                leadList.add(map);


                                //Log.e("Test data=", pos);

                                adapter = new SimpleAdapter(MainActivity.this, leadList, R.layout.list_item, new String[]{"symbol", "open", "dayHigh", "dayLow", "number"}, new int[]{R.id.txtSymbol, R.id.txtOpen, R.id.txtHigh, R.id.txtLow, R.id.txtNumber});
                                listView.setAdapter(adapter);

                                listView.setTextFilterEnabled(true);

                                //Log.e("Number:", String.valueOf(listView.getCount()));
                                //txtNumber.setText(String.valueOf(listView.getCount()));

                            }
                            //txtSymbol.setText(email);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}