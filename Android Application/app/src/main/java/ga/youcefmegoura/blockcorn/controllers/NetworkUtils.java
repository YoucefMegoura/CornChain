package ga.youcefmegoura.blockcorn.controllers;


import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class NetworkUtils {
    private static final String TAG = "Utils";

    public static final String IP_SERVER = "192.168.1.99";
    public static final int PORT_SERVER = 3300;
    public static final String SERVER_ADDRESS = "http://" + IP_SERVER + ":" + PORT_SERVER;
    public static final int TIMEOUT_CHECK_SERVER = 3000;


    public static boolean reachServer (String ip, int port, int timeout) {
        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = timeout;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            return true;
        } catch(IOException e) {
            // Handle exception
            Log.i(TAG, "aa: error" + e.getMessage());
            return false;
        }
    }



}
