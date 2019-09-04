package com.example.remote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    String title, model, name, name_c;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluslist);
        Intent intent4 = getIntent(); /*데이터 수신*/
        name = intent4.getExtras().getString("name");
        model = intent4.getExtras().getString("model");
        if(name=="0" && model=="0"){
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
                } else {
                    //호출명(name)은 나중에 음성 입력으로 처리
                    name = nameText.getText().toString();
                    model = modelText.getText().toString();
                    save();
                    finish();
                }
                break;
        }
    }

    protected void save(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //중복검사 해야함
        Collection<?> name_check =  pref.getAll().values();
        Iterator<?> it = name_check.iterator();
        while(it.hasNext()) {
            name_c = (String)it.next();
            if(name==name_c){
                Toast.makeText(this, "이름이 중복됩니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        editor.putString(name,model);
        editor.commit();
        Toast.makeText(this,"저장 완료",Toast.LENGTH_SHORT).show();
    }
}
