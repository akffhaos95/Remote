package com.example.remote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Iterator;

public class PluslistActivity extends AppCompatActivity implements View.OnClickListener {
    public final String PREFERENCE = "com.example.remote";
    TextView titleView;
    EditText nameText, modelText;
    Button applyBtn;
    String title, model, name;
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

        titleView.setText(title);
        applyBtn.setOnClickListener(this);
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
                    //호출명(name)은 나중에 음성 입력으로 처리
                    save();
                    finish();
                }
                break;
        }
    }
    protected void save(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name,model);
        editor.commit();
        Toast.makeText(this,"저장 완료",Toast.LENGTH_SHORT).show();
    }
}
