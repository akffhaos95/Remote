package com.example.remote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private SocketManager socketManager;
    TextView textTv;
    EditText textIP, textPort;
    ImageButton voiceBtn;
    Button connBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTv = findViewById(R.id.textTv);
        voiceBtn = findViewById(R.id.voiceBtn);
        connBtn = findViewById(R.id.connBtn);
        textIP = findViewById(R.id.textIP);
        textPort = findViewById(R.id.textPort);

        voiceBtn.setOnClickListener(this);
        connBtn.setOnClickListener(this);
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    break;
                case 1:
                    Toast.makeText(MainActivity.this, "Server responded : " + msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.connBtn:
                String ip = textIP.getText().toString();
                int port = Integer.parseInt(textPort.getText().toString());

                socketManager = new SocketManager(ip,port, handler);
                break;

            case R.id.voiceBtn:
                speak();
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
                                //get text array from voice intent
                                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                                //set to the text
                                socketManager.sendData(result.get(0));
                                textTv.setText(result.get(0));
                            }
                break;
            }
        }
    }

}