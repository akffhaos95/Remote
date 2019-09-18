package com.example.remote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    TextView titleView, nameText, modelText;
    Button applyBtn, nameBtn, modelBtn;
    String title, model, name, name_ck, model_ck, speakname;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluslist);
        Intent intent4 = getIntent(); /*데이터 수신*/
        name = intent4.getStringExtra("name");
        model = intent4.getStringExtra("model");
        Log.d("test2","name"+name+ ",  model"+model);
        if(name=="test"){
            title = "신규 등록";
        }else {
            title = "제품 수정";
        }
        titleView = findViewById(R.id.titleView);
        nameText = findViewById(R.id.nameText);
        modelText = findViewById(R.id.modelText);
        applyBtn = findViewById(R.id.applyBtn);
        nameBtn = findViewById(R.id.nameBtn);
        modelBtn = findViewById(R.id.modelBtn);

        titleView.setText(title);
        applyBtn.setOnClickListener(this);
        nameBtn.setOnClickListener(this);
        modelBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyBtn:
                name = nameText.getText().toString();
                model = modelText.getText().toString();
                if(name.length()==0 || model.length()==0){
                    Toast.makeText(this, "빈칸이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    save();
                    finish();
                }
                break;
            case R.id.nameBtn:
                speak();
                break;
            case R.id.modelBtn:
                //DB 액티비티 만들기
                break;
        }
    }

    protected void save(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Collection<?> name_check =  pref.getAll().keySet();
        Collection<?> model_check =  pref.getAll().keySet();
        Iterator<?> it = name_check.iterator();
        Iterator<?> it2 = model_check.iterator();
        while(it.hasNext()) {
            name_ck = (String)it.next();
            if(name == name_ck){
                Toast.makeText(this, "이름이 중복됩니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        while(it2.hasNext()){
            if(model == model_ck) {
                model_ck = (String) it.next();
                Toast.makeText(this, "모델명이 중복됩니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
