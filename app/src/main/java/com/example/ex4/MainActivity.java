package com.example.ex4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void connect(View view) {
        EditText ip = (EditText) findViewById(R.id.UserIp);
        final String ipStr =  ip.getText().toString();
        EditText port = (EditText) findViewById(R.id.UserPort);
        final int portInt = Integer.parseInt(port.getText().toString());

        TcpClient.Instance().connectToServer(ipStr, portInt);
        //TcpClient.Instance().sendMesssage("set controls/flight/rudder -1");
        // it is the first
        Intent intent = new Intent(this, MainActivityJoystick.class);
        //        //intent.putExtra("JoyStickActivity", client);
        startActivity(intent);

    }

    // Called when user taps the Connect button
//    public void ConnectMessage(View view) {
//        TextView ip = (TextView) findViewById(R.id.UserIp);
//        TextView port = (TextView) findViewById(R.id.UserPort);
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Log.d("TCP Client", "C: Connecting...");
//                    //create a socket to make the connection with the server
//                    Socket socket = new Socket("10.0.2.2", 5400);
//                    System.out.println("7");
//                } catch (Exception e) {
//                    Log.e("TCP", "C: Error", e);
//                }
//            }
//        };
//        //Thread thread = new Thread(runnable);
//        thread.start();
//
//    }
}


