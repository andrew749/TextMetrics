package com.andrew749.textmetrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by andrew on 16/04/14.
 */
public class statsadapter extends BaseAdapter {
    Data data;
    MainActivity.SortingTypes type;
    Context context;

    /**
     * @param context
     * @param data
     * @param type    determine what type of stats to use
     */
    public statsadapter(Context context, Data data, MainActivity.SortingTypes type) {
        this.context = context;
        this.data = data;
        this.type = type;
        Collections.sort(data.contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact, Contact contact2) {
                return contact2.numberOfMessagesSent - contact.numberOfMessagesSent;
            }
        });
    }

    @Override
    public int getCount() {
        return data.contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return data.getContact(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = view;
        v = inflater.inflate(R.layout.customlistview, null);
        TextView name = (TextView) v.findViewById(R.id.Name);
        TextView amount = (TextView) v.findViewById(R.id.Amount);
        name.setText("" + data.getContact(i).name);
        switch (type) {
            case Sent:
                amount.setText("" + data.getContact(i).numberOfMessagesSent);
                break;
            case Recieved:
                amount.setText("" + data.getContact(i).numberOfMessagesRecieved);
                break;
            case Conversations:
                amount.setText(""+data.getContact(i).numberOfMessages);
                break;
        }
        return v;
    }

}
