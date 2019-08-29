package com.example.remote;
/*
***Server Python Code***
import socket
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(('',443))
while 1:
    data, addr = sock.recvfrom(200)
    print(data.decode())
    print("IP:"+str(addr[0]),"PORT:"+str(addr[1]))
*/

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public SendData sendData = null;

    TextView textTv;
    EditText textIP, textPort;
    ImageButton voiceBtn;
    Button connBtn;

    String ip = null;
    int port = 0;
    String command = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceBtn = findViewById(R.id.voiceBtn);
        textTv = findViewById(R.id.textTv);
        textIP = findViewById(R.id.textIP);
        textPort = findViewById(R.id.textPort);

        voiceBtn.setOnClickListener(this);
        connBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.voiceBtn:
                ip = textIP.getText().toString();
                port = Integer.parseInt(textPort.getText().toString());
                speak();
            case R.id.listBtn:

        }
    }

    private void speak() {
        //intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");

        //start intent
        try {
            //if there was no error, show dialog
            Log.d("speak","speak start");
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            //if there was some error, get message of error and show
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //recieve voice input and handle it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                    switch(requestCode){
                        case REQUEST_CODE_SPEECH_INPUT: {
                            if(resultCode == RESULT_OK && null !=data){
                                Log.d("result","result start");
                                //get text array from voice intent
                                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                command = result.get(0);
                                //set to the text
                                sendData = new SendData();
                                sendData.start();
                                textTv.setText("client : "+result.get(0));
                            }
                break;
            }
        }
    }

    class SendData extends Thread{
        public void run(){
            try{
                Log.d("send","send start");
                DatagramSocket socket = new DatagramSocket();
                InetAddress serverAddress = InetAddress.getByName(ip);
                byte[] buf = (command).getBytes();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, port);
                socket.send(packet);
                //socket.receive(packet);
                //String msg = new String(packet.getData());
                //textTv.setText(msg);
            } catch (Exception e){

            }
        }
    }
}