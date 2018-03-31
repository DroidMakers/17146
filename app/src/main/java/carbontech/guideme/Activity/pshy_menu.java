package carbontech.guideme.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gjiazhe.multichoicescirclebutton.MultiChoicesCircleButton;

import java.util.ArrayList;
import java.util.List;

import carbontech.guideme.R;

public class pshy_menu extends AppCompatActivity {
    MultiChoicesCircleButton multiChoicesCircleButton;
    String area=null;
    String reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pshy_menu);
        reg=getIntent().getExtras().getString("reg");

        final List<MultiChoicesCircleButton.Item> buttonItems = new ArrayList<>();
        final MultiChoicesCircleButton.Item item4 = new MultiChoicesCircleButton.Item("Home", getResources().getDrawable(R.drawable.brain), 20
        );
        buttonItems.add(item4);
        final MultiChoicesCircleButton.Item item1 = new MultiChoicesCircleButton.Item("Health", getResources().getDrawable(R.drawable.brain), 60
        );

        buttonItems.add(item1);
        final MultiChoicesCircleButton.Item item2 = new MultiChoicesCircleButton.Item("Social", getResources().getDrawable(R.drawable.brain), 120);
        buttonItems.add(item2);
        final MultiChoicesCircleButton.Item item3 = new MultiChoicesCircleButton.Item("Emotional", getResources().getDrawable(R.drawable.brain), 160);
        buttonItems.add(item3);

        multiChoicesCircleButton = (MultiChoicesCircleButton) findViewById(R.id.multiChoicesCircleButton);
        multiChoicesCircleButton.setButtonItems(buttonItems);
        multiChoicesCircleButton.setOnSelectedItemListener(new MultiChoicesCircleButton.OnSelectedItemListener() {
            @Override
            public void onSelected(MultiChoicesCircleButton.Item item, int index) {

                if (item == item1) {

                    Intent in= new Intent(getApplicationContext(),Pshycometric.class);
                    area ="b";
                    in.putExtra("home",area);
                    in.putExtra("reg",reg);
                    startActivity(in);


                } else if (item == item2) {

                    Intent in= new Intent(getApplicationContext(),Pshycometric.class);
                    area ="c";
                    in.putExtra("home",area);
                    in.putExtra("reg",reg);
                    startActivity(in);

                } else if (item == item3) {

                    Intent in= new Intent(getApplicationContext(),Pshycometric.class);
                    area ="d";
                    in.putExtra("home",area);
                    in.putExtra("reg",reg);
                    startActivity(in);

                } else if (item == item4) {

                    Intent in= new Intent(getApplicationContext(),Pshycometric.class);
                    area ="a";
                    in.putExtra("home",area);
                    in.putExtra("reg",reg);
                    startActivity(in);

                }


            }
        });

    }
}
