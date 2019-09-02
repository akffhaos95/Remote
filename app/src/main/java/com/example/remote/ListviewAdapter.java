package com.example.remote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList, arrayList2;
    private ViewHolder viewHolder;

    public ListviewAdapter(Context context, ArrayList<String> arrayList, ArrayList<String> arrayList2){
        this.context = context;
        this.arrayList=arrayList;
        this.arrayList2=arrayList2;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
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
        viewHolder.nextText.setText(arrayList.get(i));
        viewHolder.nextText2.setText(arrayList2.get(i));
        return view;
    }
    public class ViewHolder {
        private TextView nextText;
        private TextView nextText2;
        public ViewHolder(View convertView) {
            nextText = convertView.findViewById(R.id.textViewName);
            nextText2 = convertView.findViewById(R.id.textViewModel);
        }
    }
}

