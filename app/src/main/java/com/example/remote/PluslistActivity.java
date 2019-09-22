package com.example.remote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

public class PluslistActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public final String PREFERENCE = "com.example.remote";
    CheckData checkData = null;
    TextView nameText;
    EditText modelText;
    Button applyBtn, nameBtn, modelBtn;
    String model, name, speakname, ip="", res="";
    int port=0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluslist);

        nameText = findViewById(R.id.nameText);
        modelText = findViewById(R.id.modelText);
        applyBtn = findViewById(R.id.applyBtn);
        nameBtn = findViewById(R.id.nameBtn);
        modelBtn = findViewById(R.id.modelBtn);

        applyBtn.setOnClickListener(this);
        nameBtn.setOnClickListener(this);
        modelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nameBtn:
                speak();
                break;
            case R.id.modelBtn:
                model = modelText.getText().toString();
                if (model.length()==0) {
                    Toast.makeText(this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences socketpref = getSharedPreferences("socket", MODE_PRIVATE);
                    ip = socketpref.getString("IP", "");
                    port = socketpref.getInt("PORT", 0);
                    if (ip == "" || port == 0) {
                        Toast.makeText(this, "IP와 PORT가 입력되어있지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        checkData = new CheckData();
                        checkData.start();
                    }
                }
                break;
            case R.id.applyBtn:
                name = nameText.getText().toString();
                model = modelText.getText().toString();
                if(name.length()==0 || model.length()==0){
                    Toast.makeText(this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(res=="Yes"){
                    save();
                    finish();
                }
                else {
                    Toast.makeText(this,"모델명 체크를 해주시길 바랍니다.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    class CheckData extends Thread{
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress serverAddress = InetAddress.getByName(ip);
                byte[] buf = ("$"+model).getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, port);
                socket.send(packet);
                byte[] buf2=("aaa").getBytes();
                packet = new DatagramPacket(buf2,buf2.length,serverAddress,port);
                socket.receive(packet);
                res = new String(packet.getData());
                Log.d("test2",res+" : "+model);
                modelBtn.setText(res);
            } catch (Exception e) { }
        }
    }

    protected void save(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name,model);
        editor.commit();
        Toast.makeText(this,"저장 완료",Toast.LENGTH_SHORT).show();
    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "호출명 말해주세요");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null !=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speakname = result.get(0);
                    nameText.setText(speakname);
                }
                break;
            }
        }
    }
}
