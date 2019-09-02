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
import java.util.Collection;
import java.util.Iterator;

public class ListviewActivity extends AppCompatActivity implements View.OnClickListener {
    public final String PREFERENCE = "com.example.remote";
    private Context context;
    private ArrayList<String> arrayList, arrayList2;
    private ListView listView;
    private ListviewAdapter listviewAdapter;
    private ImageButton plus;
    private String model_l, name_l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        this.context = getApplicationContext();
        listView = findViewById(R.id.listview);
        plus = findViewById(R.id.plus);
        plus.setOnClickListener(this);
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Collection<?> model = pref.getAll().keySet();
        Collection<?> name =  pref.getAll().values();
        Iterator<?> it = name.iterator();
        Iterator<?> it2 = model.iterator();
        while(it.hasNext()) {
            name_l = (String)it.next();
            model_l = (String)it2.next();
            arrayList.add(name_l);
            arrayList2.add(model_l);
        }
        listviewAdapter = new ListviewAdapter(context, arrayList, arrayList2);
        listView.setAdapter(listviewAdapter);
    }
    @Override
    protected void onPause(){
        super.onPause();
        arrayList.clear();
        arrayList2.clear();
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
