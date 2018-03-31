package carbontech.guideme.Activity;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import carbontech.guideme.LoginReg.LoginActivity;
import carbontech.guideme.Other.MySingleton;
import carbontech.guideme.R;
import carbontech.guideme.chat.Login;
import carbontech.guideme.chat.firebaseLogin;
import carbontech.guideme.helper.SQLiteHandler;
import carbontech.guideme.helper.SessionManager;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtReg;
    private TextView dob;
    private TextView uni;
    private TextView col;
    private TextView phone;

    private Button btnLogout;
    private ProgressDialog pd;
String number;
    private SQLiteHandler db;
    private SessionManager session;
    MultiChoicesCircleButton multiChoicesCircleButton;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtReg = (TextView) findViewById(R.id.reg);
        dob=(TextView) findViewById(R.id.Dob);
        uni = (TextView) findViewById(R.id.univ);
        col=(TextView) findViewById(R.id.College);
        phone=(TextView) findViewById(R.id.phone);
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Loading..");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);




        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String role = user.get("role");
        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        Toast.makeText(getApplicationContext(),"Welcome "+name+" your are a "+role,Toast.LENGTH_SHORT).show();
        // Logout button click event



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Register Number");

        // Setting Dialog Message
        alertDialog.setMessage("Enter Your University Register Number");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.prof);
        final EditText input = new EditText(getApplicationContext());
        input.setHint("your file name");
        alertDialog.setView(input);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                 number=input.getText().toString();
                loadStudent(number);
            }
        });


        // Showing Alert Message
        alertDialog.show();






        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        final MultiChoicesCircleButton.Item item4 = new MultiChoicesCircleButton.Item("Psychometric Test", getResources().getDrawable(R.drawable.brain), 20
        );
        buttonItems.add(item4);
        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Performance Test", getResources().getDrawable(R.drawable.per), 60
        );

        buttonItems.add(item1);
        final MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Career Guidancce", getResources().getDrawable(R.drawable.psy), 120);
        buttonItems.add(item2);
        final MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Log out", getResources().getDrawable(R.drawable.prof), 160);
        buttonItems.add(item3);

        multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {

                if (item == item1) {

                    Intent in= new Intent(getApplicationContext(),PerfMenu.class);
                    in.putExtra("reg",number);
                    startActivity(in);

                } else if (item == item2) {

                    Intent in= new Intent(getApplicationContext(),Login.class);
                    startActivity(in);

                } else if (item == item3) {
                    logoutUser();

                } else if (item == item4) {

                 Intent in= new Intent(getApplicationContext(),pshy_menu.class);
                 in.putExtra("reg",number);
                 startActivity(in);

                }


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
//home-17,health-16,emotion-29,social-22

                         try {

                             JSONArray jsonarray = new JSONArray(response);

                             for(int i=0; i < jsonarray.length(); i++) {

                                 JSONObject jsonobject = jsonarray.getJSONObject(i);


                                 String id = jsonobject.getString("id");
                                 String name = jsonobject.getString("name");
                                 String email = jsonobject.getString("email");
                                 String Dob = jsonobject.getString("DOB");
                                 String univ = jsonobject.getString("university");
                                 String college = jsonobject.getString("collegename");
                                 String regnumber = jsonobject.getString("registernumber");
                                 String Yop = jsonobject.getString("YOP");
                                 String phone1 = jsonobject.getString("phonenumber");
                                 txtReg.setText(regnumber);
                                 dob.setText(Dob);
                                 uni.setText("University: "+univ);
                                 col.setText("College: "+college);
                                 phone.setText("Phone: "+phone1);

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


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
