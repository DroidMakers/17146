package carbontech.guideme.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.util.ArrayList;
import java.util.List;

import carbontech.guideme.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.util.ArrayList;
import java.util.List;

import carbontech.guideme.R;
import carbontech.guideme.Teacher.Performance;

public class PerfMenu extends AppCompatActivity {

    String area=null;
    String reg;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perf_menu);
        reg=getIntent().getExtras().getString("reg");
        b=(Button)findViewById(R.id.button3);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in= new Intent(getApplicationContext(),Performance.class);
                area ="e";
                in.putExtra("home",area);
                in.putExtra("reg",reg);
                startActivity(in);


            }
        });

    }
}
