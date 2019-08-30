package com.example.remote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> array;
    private ViewHolder viewHolder;

    public ListviewAdapter(Context context, ArrayList<String> array){
        this.context = context;
        this.array=array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=LayoutInflater.from(context).inflate(R.layout.list,viewGroup,false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else viewHolder = (ViewHolder)view.getTag();

        viewHolder.nextText.setText(array.get(i));
        return view;
    }

    public class ViewHolder {
        private TextView nextText;

        public ViewHolder(View convertView) {
            nextText = convertView.findViewById(R.id.textView1);
        }
    }
}

