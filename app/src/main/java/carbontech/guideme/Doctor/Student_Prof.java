package carbontech.guideme.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import carbontech.guideme.Activity.Pshycometric;
import carbontech.guideme.Other.MySingleton;
import carbontech.guideme.R;

public class Student_Prof extends AppCompatActivity {

    TextView name,email,reg,cid,coll,home,health,emot,social;
    Button b;

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__prof);
        name=(TextView)findViewById(R.id.textView3);
        email=(TextView)findViewById(R.id.textView5);
        reg=(TextView)findViewById(R.id.textView10);
        cid=(TextView)findViewById(R.id.textView12);
        coll=(TextView)findViewById(R.id.textView8);
        home=(TextView)findViewById(R.id.textView15);
        health=(TextView)findViewById(R.id.textView17);
        emot=(TextView)findViewById(R.id.textView22);
        social=(TextView)findViewById(R.id.textView20);
        String re=getIntent().getExtras().getString("area");

        pd = new ProgressDialog(Student_Prof.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        loadStudent(re);


       b=(Button)findViewById(R.id.button2);
       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent in = new Intent(getApplicationContext(),StudentResult.class);
               in.putExtra("name",name.getText().toString());
               in.putExtra("email",email.getText().toString());

               in.putExtra("home",home.getText().toString());
               in.putExtra("health",health.getText().toString());
               in.putExtra("emotion",emot.getText().toString());
               in.putExtra("social",social.getText().toString());
               startActivity(in);


           }
       });



    }


    void loadStudent(String number){

        String url= "http://app-1521348299.000webhostapp.com/hackathon/getStudent.php?phone="+number;
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

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String nam = jsonobject.getString("name");
                                String emai = jsonobject.getString("email");
                                String Dob = jsonobject.getString("DOB");
                                String univ = jsonobject.getString("university");
                                String college = jsonobject.getString("collegename");
                                String regnumber = jsonobject.getString("registernumber");
                                String Yop = jsonobject.getString("YOP");
                                String phone1 = jsonobject.getString("phonenumber");
                                String ciid = jsonobject.getString("collegeID");
                                String heath = jsonobject.getString("psy_health");
                                String hom = jsonobject.getString("psy_home");
                                String emo = jsonobject.getString("psy_emotion");
                                String socal = jsonobject.getString("psy_social");
                                name.setText(nam);
                                email.setText(emai);
                                reg.setText(regnumber);
                                coll.setText(college);
                                cid.setText(ciid);
                                home.setText(hom);
                                health.setText(heath);
                                emot.setText(emo);
                                social.setText(socal);

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

}
