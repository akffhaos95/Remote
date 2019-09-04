package com.example.remote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ListviewActivity extends AppCompatActivity implements View.OnClickListener {
    public final String PREFERENCE = "com.example.remote";
    private Context context;
    private ListView listView;
    private ListviewAdapter listviewAdapter;
    private ArrayList<String> arrayList, arrayList2;
    private ImageButton plusBtn;
    private String model_list, name_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        this.context = getApplicationContext();
        Log.d("test2","onCreate");
        listView = findViewById(R.id.listview);
        plusBtn = findViewById(R.id.plusBtn);
        plusBtn.setOnClickListener(this);
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("test2","onResume");
        makeList();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("test2","onPause");
        clearList();
    }
    public void makeList(){
        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Collection<?> name = pref.getAll().keySet();
        Collection<?> model =  pref.getAll().values();
        Iterator<?> it = name.iterator();
        Iterator<?> it2 = model.iterator();
        while(it.hasNext()) {
            Log.d("test2","makeList");
            name_list = (String)it.next();
            model_list = (String)it2.next();
            arrayList.add(name_list);
            arrayList2.add(model_list);
        }
        listviewAdapter = new ListviewAdapter(context, arrayList, arrayList2);
        listView.setAdapter(listviewAdapter);

        Log.d("test2","makeAdapter");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test2","makeClick");
                onClick_Delete(arrayList.get(position));
            }
        });
    }
    public void clearList(){
        arrayList.clear();
        arrayList2.clear();
    }
    public void onClick_Delete(final String name){
        new AlertDialog.Builder(this)
                .setTitle("삭제")
                .setMessage(name+"을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.remove(name);
                        editor.commit();
                        Toast.makeText(ListviewActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        clearList();
                        makeList();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) { }})
                .show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.plusBtn:
                Intent intent3 = new Intent(getApplicationContext(), PluslistActivity.class);
                intent3.putExtra("name","test");
                intent3.putExtra("model","test");
                startActivityForResult(intent3,1002);
                break;
        }
    }
}
