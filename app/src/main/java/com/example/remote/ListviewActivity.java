package com.example.remote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ListviewActivity extends AppCompatActivity implements View.OnClickListener {
    public final String PREFERENCE = "com.example.remote";
    private Context context;
    private ArrayList<String> array;
    private ListView listView;
    private ListviewAdapter listviewAdapter;
    private ImageButton plus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        this.context = getApplicationContext();
        listView = findViewById(R.id.listview);
        plus = findViewById(R.id.plus);

        plus.setOnClickListener(this);

        array = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        for (int i = 0;; ++i){
            final String str = pref.getString(String.valueOf(i), "");
            if (!str.equals("")){ array.add(str); } else { break; }
        }

        listviewAdapter = new ListviewAdapter(context, array);
        listView.setAdapter(listviewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.plus:
                Intent intent3 = new Intent(getApplicationContext(), PluslistActivity.class);
                intent3.putExtra("name","0");
                intent3.putExtra("model","0");
                startActivityForResult(intent3,1002);
                break;
        }
    }
}
