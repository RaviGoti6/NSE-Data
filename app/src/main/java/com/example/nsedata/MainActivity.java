package com.example.nsedata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

/*
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
                if (orig == null)
                    orig = leadList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final HashMap<String, String> g : orig) {
                            if (g.get("symbol").toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                leadList = (ArrayList<HashMap<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notify();
    }
*/

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