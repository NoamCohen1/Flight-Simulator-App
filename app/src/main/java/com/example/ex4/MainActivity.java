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
        // get the ip the user entered
        EditText ip = (EditText) findViewById(R.id.UserIp);
        final String userIp =  ip.getText().toString();
        // get the port the user entered
        EditText port = (EditText) findViewById(R.id.UserPort);
        final int userPort = Integer.parseInt(port.getText().toString());

        TcpClient.Instance().connectToTheServer(userIp, userPort);
        // it is the first
        Intent myIntent = new Intent(this, MainActivityJoystick.class);
        startActivity(myIntent);
    }

    protected void onDestroy(){
        super.onDestroy();
        TcpClient.Instance().disConnectFromServer();
    }
}


