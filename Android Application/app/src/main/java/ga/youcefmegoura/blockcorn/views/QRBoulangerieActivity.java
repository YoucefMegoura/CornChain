package ga.youcefmegoura.blockcorn.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import ga.youcefmegoura.blockcorn.R;

import static ga.youcefmegoura.blockcorn.controllers.NetworkUtils.SERVER_ADDRESS;

public class QRBoulangerieActivity extends AppCompatActivity {

    private static final String TAG = "QRBoulangerieActivity";

    private static final String SERVER_URL = SERVER_ADDRESS + "/boulangerie";

    private TextView productId_TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide the Action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_q_r_boulangerie);

        productId_TextView = (TextView) findViewById(R.id.productId_TextView);

        AndroidNetworking.initialize(getApplicationContext());
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning Code");
        intentIntegrator.initiateScan();
    }

    //onClick Button
    public void submit(View view) {
        String productID = productId_TextView.getText().toString();
        if (productID.isEmpty() || productID.equals("Product ID")) {
            productId_TextView.setError("Veillez scanner un QR code");
            productId_TextView.requestFocus();
            return;
        }
        postRequest(productID);

        finish();
        startActivity(new Intent(getBaseContext(), DoneActivity.class));
    }

    //onClick Button
    public void scan(View view) {
        scanCode();
    }

    public void postRequest(String productId) {
        AndroidNetworking.post(SERVER_URL)
                .addBodyParameter("productId", productId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.i(TAG, "onResponse: " + response);
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(new Intent(getBaseContext(), DoneActivity.class));
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getApplicationContext(), "There is an error", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: no" + error.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String result = intentResult.getContents();
                productId_TextView.setText(result);
            } else {
                Toast.makeText(this, "No result for this QR", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}