package com.andrew749.textmetrics;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Random;

/**
 * Created by andrew on 11/04/14.
 */
public class ConversationsFragment extends Fragment {
    public DefaultRenderer defaultRenderer = new DefaultRenderer();
    public GraphicalView chartview;
    // colors
    public int[] color = {Color.GREEN, Color.RED, Color.BLACK, Color.BLUE,
            Color.RED, Color.BLACK, Color.YELLOW};
    Data results;
    ListView lview;
    LinearLayout layout;
//    LAdapter a;
//    Conversations[] conve;
    Activity activity;
    CategorySeries series = new CategorySeries("Contacts");

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }

//sets up the chart renderer
    public void setupRenderer() {
        defaultRenderer.setLegendTextSize(30);
        defaultRenderer.setChartTitle("Conversations");
        defaultRenderer.setLabelsTextSize(30f);
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setBackgroundColor(45454545);
    }

//    public void getConversations() {
//        Uri SMS_INBOX = Uri.parse("content://sms/conversations/");
//        Cursor c = activity.getContentResolver()
//                .query(SMS_INBOX, null, null, null, null);
//
//        String[] count = new String[c.getCount()];
//        String[] thread_id = new String[c.getCount()];
//        conve = new Conversations[c.getCount()];
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            count[i] = c.getString(c.getColumnIndexOrThrow("msg_count"))
//                    .toString();
//            thread_id[i] = c.getString(c.getColumnIndexOrThrow("thread_id"))
//                    .toString();
//
//            Log.d("count", count[i]);
//            Log.d("thread", thread_id[i]);
//            String a = contactAddress(thread_id[i]);
//            a = addressToContact(a);
//            Log.d("", a);
//            conve[i] = new Conversations(a, Integer.parseInt(count[i]));
//            c.moveToNext();
//        }
//        c.close();
//    }
//
//    public String contactAddress(String threadid) {
//        String address = "";
//        Uri inbox = Uri.parse("content://sms/inbox");
//        Cursor c = activity.getContentResolver().query(inbox, null,
//                "thread_id=" + threadid, null, null);
//        if (c.moveToFirst()) {
//            address = c.getString(c.getColumnIndexOrThrow("address"));
//            Log.d("address=", address);
//        }
//        c.close();
//        return address;
//    }
//
//    public String addressToContact(String address) {
//        String name = address;
//        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
//        try {
//            Cursor c = activity.getContentResolver().query(uri,
//                    new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
//            c.moveToFirst();
//            if (c.getCount() > 0) {
//                try {
//                    if (!(c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
//                            .equals(""))) {
//                        name = c.getString(c
//                                .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//                    }
//                } catch (SQLiteException e) {
//                }
//                Log.d("Name", name);
//            }
//            c.close();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            Log.d("TextMetrics", "Failing Address:" + name);
//        }
//
//        return name;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        lview = (ListView) view.findViewById(R.id.listView1);
        layout = (LinearLayout) view.findViewById(R.id.chart);
        series.clear();


Bundle b=getArguments();
        Data data=(Data)b.getSerializable("data");
        for(int i=0;i<data.contacts.size();i++){
            series.add(data.getContact(i).name,data.getContact(i).numberOfMessages);
        }
        statsadapter adapt=new statsadapter(getActivity(),data, MainActivity.SortingTypes.Conversations);
        Random rn = new Random();
        for (int i = 0; i < data.contacts.size(); i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            seriesRenderer.setColor(Color.argb(255, rn.nextInt(256),
                    rn.nextInt(256), rn.nextInt(256)));
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        setupRenderer();
        chartview = ChartFactory.getPieChartView(activity, series, defaultRenderer);
        layout.addView(chartview);
        lview.setAdapter(adapt);
        chartview.repaint();
        AdView layout = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        layout.loadAd(adRequest);
        return view;
    }

}
