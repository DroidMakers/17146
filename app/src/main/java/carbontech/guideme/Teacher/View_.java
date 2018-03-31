package carbontech.guideme.Teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import carbontech.guideme.Activity.Question;
import carbontech.guideme.Activity.Result;
import carbontech.guideme.Other.MySingleton;
import carbontech.guideme.R;

public class View_ extends AppCompatActivity {



    ListView listView;
    private ProgressDialog pd;

    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_);
         final String name=getIntent().getExtras().getString("name");
        final String reg=getIntent().getExtras().getString("reg");
        listView = (ListView) findViewById(R.id.listView);
        pd = new ProgressDialog(View_.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                String s = listView.getItemAtPosition(i).toString();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent in = new Intent(View_.this,Student_Prof.class);

                in.putExtra("area",s);
                in.putExtra("name",name);
                in.putExtra("reg",reg);
                startActivity(in);


            }
        });

        new GetJSON().execute();

    }





    class GetJSON extends AsyncTask<Void, Void, String> {

        String area=getIntent().getExtras().getString("col");
        String urlWebService ="http://app-1521348299.000webhostapp.com/hackathon/getStudentDoc.php?col="+area;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            try {
                loadIntoListView(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlWebService);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }
        }
    }



    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] heroes = new String[jsonArray.length()];
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            //heroes[i] = obj.getString("name");
            heroes[i] = obj.getString("registernumber");

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        listView.setAdapter(arrayAdapter);

    }

}