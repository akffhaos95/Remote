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
import android.content.SharedPreferences;
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
    Button listBtn;

    String ip = null;
    int port = 0;
    String command = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voiceBtn = findViewById(R.id.voiceBtn);
        listBtn = findViewById(R.id.listBtn);
        textTv = findViewById(R.id.textTv);
        textIP = findViewById(R.id.textIP);
        textPort = findViewById(R.id.textPort);

        voiceBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.voiceBtn:
                if(textIP.getText().toString().length()==0 || textPort.getText().toString().length()==0){
                    Toast.makeText(this, "IP와 Port를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    ip = textIP.getText().toString();
                    port = Integer.parseInt(textPort.getText().toString());
                    speak();
                }
                break;
            case R.id.listBtn:
                Intent intent2= new Intent(getApplicationContext(), ListActivity.class);
                startActivityForResult(intent2, 1001);
                break;
        }
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "명령을 말해주세요");

        //start intent
        try { startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT); }
        catch (Exception e){ Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show(); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                    switch(requestCode){
                        case REQUEST_CODE_SPEECH_INPUT: {
                            if(resultCode == RESULT_OK && null !=data){
                                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                command = result.get(0);
                                textChange();
                                if(command=="0"){
                                    textTv.setText("존재하지 않는 제품입니다. 제품 리스트를 추가해주세요.");
                                }
                                else {
                                    sendData = new SendData();
                                    sendData.start();
                                    textTv.setText("client : " + command);
                                }
                            }
                break;
            }
        }
    }

    public void textChange(){
        SharedPreferences sf = getSharedPreferences("model", MODE_PRIVATE);
        if(command.contains("TV")){
            command = command.replace("TV","테레비");
        }
        else {
            command = "0";
        }
    }

    class SendData extends Thread{
        public void run(){
            try{
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