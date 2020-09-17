package ga.youcefmegoura.blockcorn.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import ga.youcefmegoura.blockcorn.R;
import ga.youcefmegoura.blockcorn.controllers.NetworkUtils;

import static ga.youcefmegoura.blockcorn.controllers.NetworkUtils.IP_SERVER;
import static ga.youcefmegoura.blockcorn.controllers.NetworkUtils.PORT_SERVER;
import static ga.youcefmegoura.blockcorn.controllers.NetworkUtils.TIMEOUT_CHECK_SERVER;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int SPLASH_TIME_OUT = 2000;

    private RelativeLayout splashScreen_RelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hide the Action bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);

        splashScreen_RelativeLayout = (RelativeLayout) findViewById(R.id.splashScreen_RelativeLayout);

        /*************** Splash Screen **************/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isServerReachable();
            }
        }, SPLASH_TIME_OUT);

    }


    public void isServerReachable() {
        Thread reachServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /*************** Trying to reach the server **************/
                    if (NetworkUtils.reachServer(IP_SERVER, PORT_SERVER, TIMEOUT_CHECK_SERVER)) {
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        Toast.makeText(getBaseContext(), "Connected", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Snackbar.make(splashScreen_RelativeLayout, "Not Connected; The server cannot be reached!", Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        reachServerThread.start();
    }


}

