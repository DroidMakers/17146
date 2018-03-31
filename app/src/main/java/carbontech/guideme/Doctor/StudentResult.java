package carbontech.guideme.Doctor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import carbontech.guideme.Activity.pshy_menu;
import carbontech.guideme.R;

public class StudentResult extends AppCompatActivity implements OnChartValueSelectedListener {
    MultiChoicesCircleButton multiChoicesCircleButton;
    TextView name,email,reg,cid,coll,home,health,emot,social,feedback;
    Button b;
    String na,emo,ho,he,em,so;
    private static final String REGISTER_URL = "http://app-1521348299.000webhostapp.com/hackathon/uploadScore.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        name=(TextView)findViewById(R.id.textView6);
        email=(TextView)findViewById(R.id.textView7);
        TextView overall=(TextView)findViewById(R.id.textView11);


        na=getIntent().getExtras().getString("name");
        emo=getIntent().getExtras().getString("email");
        ho=getIntent().getExtras().getString("home");
        he=getIntent().getExtras().getString("health");
        em=getIntent().getExtras().getString("emotion");
        so=getIntent().getExtras().getString("social");

        final int homper=(Integer.parseInt(ho)*100/17);
        final int heaper=(Integer.parseInt(he)*100/16);
        final int emper=(Integer.parseInt(em)*100/27);
        final int soper=(Integer.parseInt(so)*100/29);
        name.setText(na);
        email.setText(emo);

        int overal=((Integer.parseInt(ho)+Integer.parseInt(he)+Integer.parseInt(em)+Integer.parseInt(so))*100)/89;


      if(overal <=40){
          overall.setText("Overall Score "+String.valueOf(overal)+"% ");
          overall.setTextColor(Color.RED);
          Toast.makeText(getApplicationContext(),"Needs Serious Attention",Toast.LENGTH_LONG).show();

          Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
          emailIntent.setData(Uri.parse("mailto:"+emo));
          emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
          emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+na+"\n We are from the Pshychometric team we would like to collect your feedback form");
          startActivity(emailIntent);




      }else if(overal >40 && overal <=70  ){

          overall.setText("Overall Score "+String.valueOf(overal)+"%");
          overall.setTextColor(Color.YELLOW);
          Toast.makeText(getApplicationContext(),"AVERAGE, Needs to be Improved",Toast.LENGTH_LONG).show();

      }
      else if(overal >71 && overal <=100 ){

          overall.setText("Overall Score "+String.valueOf(overal)+"%");
          overall.setTextColor(Color.GREEN);
          Toast.makeText(getApplicationContext(),"GOOD",Toast.LENGTH_LONG).show();

      }

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);


        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(homper, 0));
        yvalues.add(new Entry(heaper, 1));
        yvalues.add(new Entry(emper, 2));
        yvalues.add(new Entry(soper, 3));

        PieDataSet dataSet = new PieDataSet(yvalues, "Analysis");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Home %");
        xVals.add("Health %");
        xVals.add("Emotions %");
        xVals.add("Social %");


        PieData data = new PieData(xVals, dataSet);
        // In Percentage term
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        pieChart.setDescription("This is Pie Chart");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(25f);
        pieChart.setHoleRadius(25f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateXY(1400, 1400);




        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        final MultiChoicesCircleButton.Item item4 = new MultiChoicesCircleButton.Item("Send Email", getResources().getDrawable(R.drawable.brain), 30
        );
        buttonItems.add(item4);
        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Upload Score", getResources().getDrawable(R.drawable.per), 90
        );

        buttonItems.add(item1);

        final MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Log out", getResources().getDrawable(R.drawable.prof), 150);
        buttonItems.add(item3);

        multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {

                if (item == item1) {

                    register(na,emo,String.valueOf(heaper),String.valueOf(homper),String.valueOf(emper),String.valueOf(soper));

                } else if (item == item3) {


                } else if (item == item4) {

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+emo));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+na+"\n We are from the Pshychometric team we would like to collect your feedback form");
                    startActivity(emailIntent);

                }


            }
        });





    }




    public void register(String name,String reg,String home,String health,String emotion,String social) {
        String urlSuffix = "?name="+name+"&reg="+reg+"&home="+home+"&health="+health+"&emotion="+emotion+"&social="+social;
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


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }
}
