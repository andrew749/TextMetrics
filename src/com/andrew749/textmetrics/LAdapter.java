package com.andrew749.textmetrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LAdapter extends BaseAdapter {
    Conversations[] conversations;
    Context c;

    public LAdapter(Context context, Conversations[] conversations) {
        this.conversations = conversations;
        c = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        v = inflater.inflate(R.layout.customlistview, null);
        TextView name = (TextView) v.findViewById(R.id.Name);
        TextView amount = (TextView) v.findViewById(R.id.Amount);
        name.setText("" + conversations[position].address);
        amount.setText("" + conversations[position].messages);
        return v;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return conversations.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
}
