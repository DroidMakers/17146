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

import carbontech.guideme.Doctor.View_Student;
import carbontech.guideme.LoginReg.LoginActivity;
import carbontech.guideme.Other.MySingleton;
import carbontech.guideme.R;
import carbontech.guideme.Teacher.View_;
import carbontech.guideme.chat.Login;
import carbontech.guideme.chat.firebaseLogin;
import carbontech.guideme.helper.SQLiteHandler;
import carbontech.guideme.helper.SessionManager;

public class Teacher extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtReg;
    private TextView col;
  String college,nam,regnumber;
    private Button btnLogout;
    private ProgressDialog pd;

    private SQLiteHandler db;
    private SessionManager session;
    MultiChoicesCircleButton multiChoicesCircleButton;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtReg = (TextView) findViewById(R.id.reg);
        col=(TextView) findViewById(R.id.College);
        pd = new ProgressDialog(Teacher.this);
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



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Teacher.this);

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
                String number=input.getText().toString();
                loadStudent(number);
            }
        });


        // Showing Alert Message
        alertDialog.show();






        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();

        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Guide Student", getResources().getDrawable(R.drawable.per), 30
        );

        buttonItems.add(item1);
        final MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Student List", getResources().getDrawable(R.drawable.psy), 90);
        buttonItems.add(item2);
        final MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Log out", getResources().getDrawable(R.drawable.prof), 150);
        buttonItems.add(item3);

        multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {

                if (item == item1) {

                    Intent in= new Intent(getApplicationContext(),Login.class);
                    startActivity(in);

                } else if (item == item2) {

                    Intent in = new Intent(getApplicationContext(), View_.class);
                    in.putExtra("col",college);
                    in.putExtra("name",nam);
                    in.putExtra("reg",regnumber);
                    startActivity(in);
                } else if (item == item3) {
                    logoutUser();

                }


            }
        });

    }


    void loadStudent(String number){

        String url= "http://app-1521348299.000webhostapp.com/hackathon/getTeacher.php?phone="+number;
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
                                 nam = jsonobject.getString("name");
                                String email = jsonobject.getString("email");
                                college = jsonobject.getString("collegename");
                                 regnumber = jsonobject.getString("fid");
                                txtReg.setText(regnumber);
                                col.setText("College: "+college);

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
        Intent intent = new Intent(Teacher.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
