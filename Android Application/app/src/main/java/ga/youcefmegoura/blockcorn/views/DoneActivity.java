package ga.youcefmegoura.blockcorn.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ga.youcefmegoura.blockcorn.R;

public class DoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_done);


    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();

    }
}