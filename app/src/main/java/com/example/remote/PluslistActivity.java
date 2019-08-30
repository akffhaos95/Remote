package com.example.remote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PluslistActivity extends AppCompatActivity implements View.OnClickListener {
    TextView titleView;
    EditText nameText, modelText;
    Button applyBtn;
    String title, model, name;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluslist);
        Intent intent4 = getIntent(); /*데이터 수신*/
        name = intent4.getExtras().getString("name");
        model = intent4.getExtras().getString("model");
        if(name=="0" || model=="0"){
            title = "신규 등록";
        }else title = "제품 수정";

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
                if(nameText.getText().toString().length()==0 || modelText.getText().toString().length()==0){
                    Toast.makeText(this, "빈칸을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                name = nameText.getText().toString();
                model = modelText.getText().toString();
                save();
                break;
        }
    }

    private void save(){

    }
}
