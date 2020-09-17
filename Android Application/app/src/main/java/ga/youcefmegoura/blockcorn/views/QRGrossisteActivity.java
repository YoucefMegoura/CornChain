package ga.youcefmegoura.blockcorn.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import org.json.JSONObject;

import java.util.Collection;

import ga.youcefmegoura.blockcorn.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static ga.youcefmegoura.blockcorn.controllers.BluetoothUtils.BLUTOOTH_DEVICE_MAC;
import static ga.youcefmegoura.blockcorn.controllers.NetworkUtils.SERVER_ADDRESS;

public class QRGrossisteActivity extends AppCompatActivity {

    private static final String TAG = "QRGrossisteActivity";

    private SimpleBluetoothDeviceInterface deviceInterface;
    private BluetoothManager bluetoothManager;

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


    private static final String SERVER_URL = SERVER_ADDRESS + "/grossiste";

    private TextView productId_TextView;
    private EditText productTemp_EditText;
    private EditText productHum_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide the Action bar
        getSupportActionBar().hide();

        setContentView(R.layout.activity_q_r_grossiste);

        productId_TextView = (TextView) findViewById(R.id.productId_TextView);
        productTemp_EditText = (EditText) findViewById(R.id.productTemp_EditText);
        productHum_EditText = (EditText) findViewById(R.id.productHum_EditText);

        // Setup our BluetoothManager
        bluetoothManager = BluetoothManager.getInstance();
        if (bluetoothManager == null) {
            // Bluetooth unavailable on this device :( tell the user
            Toast.makeText(this, "Bluetooth not available.", Toast.LENGTH_LONG).show(); // Replace context with your context instance.
            finish();
        }

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
        String keyTemperature = "temp2";
        String keyHumidity = "hum2";
        String productID = productId_TextView.getText().toString();
        String productTemperature = productTemp_EditText.getText().toString();
        String productHumidity = productHum_EditText.getText().toString();


        if (productTemperature.trim().isEmpty()) {
            productTemp_EditText.setError("Veillez entrer la temperature!");
            productTemp_EditText.requestFocus();
            return;
        }

        if (productHumidity.trim().isEmpty()) {
            productHum_EditText.setError("Veillez entrer l'humidité!");
            productHum_EditText.requestFocus();
            return;
        }

        if (productID.isEmpty() || productID.equals("Product ID")) {
            productId_TextView.setError("Veillez scanner un QR code!");
            productId_TextView.requestFocus();
            return;
        }

        getPostRequest(productID, keyTemperature, productTemperature, keyHumidity, productHumidity);

        finish();
        startActivity(new Intent(getBaseContext(), DoneActivity.class));

    }

    //onClick Button
    public void scan(View view) {
        scanCode();
    }

    public void getPostRequest(String productId, String keyTemperature, String temperature, String keyHumidity, String humidity) {
        AndroidNetworking.post(SERVER_URL)
                .addBodyParameter("productId", productId)
                .addBodyParameter(keyTemperature, temperature)
                .addBodyParameter(keyHumidity, humidity)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response);

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
                productId_TextView.setText(result.toString());
            } else {
                Toast.makeText(this, "No result for this QR", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //onClick Button
    public void connectBluetooth(View view) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        connectDevice(BLUTOOTH_DEVICE_MAC);

    }

    //Bluetooth connection
    private void getDevices() {
        Collection<BluetoothDevice> pairedDevices = bluetoothManager.getPairedDevicesList();
        for (BluetoothDevice device : pairedDevices) {
            Log.d("My Bluetooth App", "Device name: " + device.getName());
            Log.d("My Bluetooth App", "Device MAC Address: " + device.getAddress());
        }
    }

    private void connectDevice(String mac) {
        bluetoothManager.openSerialDevice(mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onError);
    }

    private void onConnected(BluetoothSerialDevice connectedDevice) {
        // You are now connected to this device!
        // Here you may want to retain an instance to your device:
        deviceInterface = connectedDevice.toSimpleDeviceInterface();

        // Listen to bluetooth events
        deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, this::onError);

        getDevices();
        // Let's send a message:
        // deviceInterface.sendMessage("Hello world!");
    }

    private void onMessageSent(String message) {
        // We sent a message! Handle it here.
        Toast.makeText(this, "Sent a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onMessageReceived(String message) {
        try {

            JSONObject obj = new JSONObject(message);
            String tempValue = obj.getString("temp");
            String humValue = obj.getString("hum");

            productTemp_EditText.setText(tempValue + " C°");
            productHum_EditText.setText(humValue + " %");


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + message + "\"");
        }

        // We received a message! Handle it here.
        Log.i(TAG, "onMessageReceived: " + "Received a message! Message was: " + message);
    }

    private void onError(Throwable error) {
        Log.i(TAG, "onError: " + error.getMessage());
    }

    private void disconnect() {
        // Remember to destroy your instance after closing as it will no longer function!
        // Disconnect all devices
        bluetoothManager.close();
    }

    //onClick Button
    public void disconnectBluetooth(View view) {
        disconnect();
        finish();
    }


    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    @Override
    protected void onStop() {
        disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }
}