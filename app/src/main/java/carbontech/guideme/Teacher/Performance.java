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

public class Performance extends AppCompatActivity {



    ListView listView;
    private ProgressDialog pd;
    Button b;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        final String area=getIntent().getExtras().getString("reg");
        listView = (ListView) findViewById(R.id.listView);
        b=(Button)findViewById(R.id.button2);
        pd = new ProgressDialog(Performance.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                calcresult(area);
                Intent in = new Intent(getApplicationContext(),Result.class);
                startActivity(in);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                String s = listView.getItemAtPosition(i).toString();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent in = new Intent(Performance.this,Question.class);
                in.putExtra("area",s);
                startActivity(in);


            }
        });

       new GetJSON().execute();

    }


    void calcresult(String number){

        String url= "http://app-1521348299.000webhostapp.com/hackathon/successperf.php?submit=yes&reg="+number;
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();


                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }


    class GetJSON extends AsyncTask<Void, Void, String> {

        String area=getIntent().getExtras().getString("home");
        String urlWebService ="http://app-1521348299.000webhostapp.com/hackathon/getquestion.php?area="+area;
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
            heroes[i] = obj.getString("sno");

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        listView.setAdapter(arrayAdapter);

    }

}