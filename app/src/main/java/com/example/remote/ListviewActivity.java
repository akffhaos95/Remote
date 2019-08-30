package com.example.remote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ListviewActivity extends AppCompatActivity implements View.OnClickListener {
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
        array.add("한라산");
        array.add("백두산");
        array.add("월출산");
        array.add("금강산");
        array.add("마니산");
        array.add("설악산");
        array.add("관악산");
        array.add("지리산");
        array.add("대둔산");
        array.add("도봉산");

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
