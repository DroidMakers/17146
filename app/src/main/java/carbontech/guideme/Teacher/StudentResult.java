package carbontech.guideme.Teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import carbontech.guideme.Activity.pshy_menu;
import carbontech.guideme.R;

public class StudentResult extends AppCompatActivity {
    MultiChoicesCircleButton multiChoicesCircleButton;
    TextView name,email,reg,cid,coll,home,health,emot,perf,feedback;
    Button b;
    String na,emo,treg,tname,em,so;
    private static final String REGISTER_URL = "http://app-1521348299.000webhostapp.com/hackathon/uploadScoreTeacher.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result2);

        name=(TextView)findViewById(R.id.textView5);
        email=(TextView)findViewById(R.id.textView6);
        perf=(TextView)findViewById(R.id.textView7);
        feedback=(TextView)findViewById(R.id.textView8);
        na=getIntent().getExtras().getString("name");
        emo=getIntent().getExtras().getString("email");
        so=getIntent().getExtras().getString("social");
        treg=getIntent().getExtras().getString("reg");
        tname=getIntent().getExtras().getString("name");


        final int soper=(Integer.parseInt(so)*100/29);
        name.setText(na);
        email.setText(emo);
        perf.setText(String.valueOf(soper)+"%");

        if(soper <=40){

            feedback.setText("Below average need attention");

        }else if(soper >=40){

            feedback.setText("Good");
        }



        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        final MultiChoicesCircleButton.Item item4 = new MultiChoicesCircleButton.Item("Send Email", getResources().getDrawable(R.drawable.brain), 20
        );
        buttonItems.add(item4);
        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Upload Score", getResources().getDrawable(R.drawable.per), 60
        );

        buttonItems.add(item1);

        final MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Log out", getResources().getDrawable(R.drawable.prof), 160);
        buttonItems.add(item3);

        multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {

                if (item == item1) {

                    register(na,emo,String.valueOf(soper),tname,treg);

                } else if (item == item3) {


                } else if (item == item4) {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+emo));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+na+"\n We are from the Performance team we would like to collect your feedback form");
                    startActivity(emailIntent);

                }


            }
        });





    }




    public void register(String name,String reg,String social,String tname,String treg) {
        String urlSuffix = "?name="+name+"&reg="+reg+"&social="+social+"&tname="+tname +"&treg="+treg;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(StudentResult.this, "Please Wait ....","Uploading ..", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }












}
