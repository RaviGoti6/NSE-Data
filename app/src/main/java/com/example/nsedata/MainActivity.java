package com.example.nsedata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter adptr;

    String symbol, open, high, low;

    ArrayList<HashMap<String, String>> leadList;

    OkHttpClient client = new OkHttpClient();

    TextView txtSymbol;

    public String url = "https://nse-data1.p.rapidapi.com/nifty_fifty_indices_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);

        leadList = new ArrayList<>();

        txtSymbol = (TextView) findViewById(R.id.txtSymbol);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);

        HashMap<String, String> map = new HashMap<>();

        map.put(symbol, "sdd");
        map.put(open, "sdf");
        map.put(high, "ag");
        map.put(low, "fg");

        leadList.add(map);


    }

    public class OkHttpHandler extends AsyncTask<Void, Void, String> {

        OkHttpClient client = new OkHttpClient();

/*        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");

            pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            //Log.e("SAFALM5=", contacts.get("success").toString() + contacts.get("message").toString());
            // Toast.makeText(getApplicationContext(), contacts.get("success").toString() + contacts.get("message").toString(), Toast.LENGTH_SHORT).show();
            //String empId=contacts.get("message").toString();
            //se.setempId(contacts.get("message").toString());
            //   String eid=se.getempId().toString();
            //    Log.e("Safalam 6:",eid);

            adptr =new SimpleAdapter(MainActivity.this, leadList, R.layout.list_item, new String[]{symbol, open, high, low},new int[]{R.id.txtSymbol,R.id.txtOpen,R.id.txtHigh,R.id.txtLow});
            listView.setAdapter(adptr);


            //  if (contacts.get("success").toString().equals("1")) {
//                Intent i = new Intent(AddSelfLeadActivity.this, MainActivity.class);
//                startActivity(i);
//                finish();
            //       Toast.makeText(SelfLeadListActivity.this, "Lead added successfully...", Toast.LENGTH_SHORT).show();
            //   }

            pDialog.dismiss();
        }*/

        @Override
        protected String doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url("https://nse-data1.p.rapidapi.com/nifty_fifty_indices_data")
                    .get()
                    .addHeader("x-rapidapi-key", "066264504fmsh752b52265a4ce2bp1a74d7jsn437e3e183038")
                    .addHeader("x-rapidapi-host", "nse-data1.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
}