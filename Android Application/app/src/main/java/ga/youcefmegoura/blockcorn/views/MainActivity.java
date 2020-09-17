package ga.youcefmegoura.blockcorn.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ga.youcefmegoura.blockcorn.R;

public class MainActivity extends AppCompatActivity {
    private Button minoterie_Button;
    private Button grossiste_Button;
    private Button boulangerie_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        minoterie_Button = (Button) findViewById(R.id.minoterie_Button);
        grossiste_Button = (Button) findViewById(R.id.grossiste_Button);
        boulangerie_Button = (Button) findViewById(R.id.boulangerie_Button);

    }


    //onClick Button
    public void minoterie(View view) {
        startActivity(new Intent(MainActivity.this, QRMinoterieActivity.class));
    }

    //onClick Button
    public void grossiste(View view) {
        startActivity(new Intent(MainActivity.this, QRGrossisteActivity.class));
    }

    //onClick Button
    public void boulangerie(View view) {
        startActivity(new Intent(MainActivity.this, QRBoulangerieActivity.class));
    }

}