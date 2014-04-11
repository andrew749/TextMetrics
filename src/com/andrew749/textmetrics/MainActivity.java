package com.andrew749.textmetrics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Random;

public class MainActivity extends Activity {
    public static int totalmessages = 0;
    static int total_texts;
    public DefaultRenderer defaultRenderer = new DefaultRenderer();
    public GraphicalView chartview;
    // colors
    public int[] color = {Color.GREEN, Color.RED, Color.BLACK, Color.BLUE,
            Color.RED, Color.BLACK, Color.YELLOW};
    ListView view;
    LinearLayout layout;
    CategorySeries series = new CategorySeries("Contacts");
    ProgressDialog progDailog;

    // ArrayAdapter a;
    LAdapter a;
    Conversations[] conve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (ListView) findViewById(R.id.listView1);
        layout = (LinearLayout) findViewById(R.id.chart);
        progDailog = new ProgressDialog(this);

        getConversations();
        series.clear();
        for (int i = 0; i < conve.length; i++) {
            series.add(conve[i].address, conve[i].messages);
        }
        // a = new ArrayAdapter<Conversations>(this,
        // android.R.layout.simple_list_item_1, conve);
        a = new LAdapter(this, conve);
        Random rn = new Random();
        for (int i = 0; i < conve.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            // seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            seriesRenderer.setColor(Color.argb(255, rn.nextInt(256),
                    rn.nextInt(256), rn.nextInt(256)));
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        setupRenderer();
        chartview = ChartFactory.getPieChartView(this, series, defaultRenderer);
        layout.addView(chartview);
        view.setAdapter(a);
        chartview.repaint();
        AdView layout = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        Log.d("Ad", "Loading");
        layout.loadAd(adRequest);
        getData task = new getData(this);
        task.execute();
        Log.d("task", "running task");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // calculate the percent that a contact contacts
    public double calculatePercent(int total, int number) {
        return (double) number / (double) total;
    }

    public void showDialog() {
        progDailog.setMessage("Loading Contacts...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.show();
        Log.d("Showing Dialog", "MEOW");
    }

    public void closeDialog() {
        progDailog.dismiss();
    }

    public void setupRenderer() {
        defaultRenderer.setLegendTextSize(30);
        defaultRenderer.setChartTitle("Contacts Breakdown");
        defaultRenderer.setLabelsTextSize(30f);
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setBackgroundColor(45454545);
    }

    public void getConversations() {
        Uri SMS_INBOX = Uri.parse("content://sms/conversations/");
        Cursor c = getContentResolver()
                .query(SMS_INBOX, null, null, null, null);

        String[] count = new String[c.getCount()];
        String[] thread_id = new String[c.getCount()];
        conve = new Conversations[c.getCount()];

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            count[i] = c.getString(c.getColumnIndexOrThrow("msg_count"))
                    .toString();
            thread_id[i] = c.getString(c.getColumnIndexOrThrow("thread_id"))
                    .toString();

            Log.d("count", count[i]);
            Log.d("thread", thread_id[i]);
            String a = contactAddress(thread_id[i]);
            a = addressToContact(a);
            Log.d("", a);
            conve[i] = new Conversations(a, Integer.parseInt(count[i]));
            c.moveToNext();
        }
        c.close();
    }

    public String contactAddress(String threadid) {
        String address = "";
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(inbox, null,
                "thread_id=" + threadid, null, null);
        if (c.moveToFirst()) {
            address = c.getString(c.getColumnIndexOrThrow("address"));
            Log.d("address=", address);
        }
        c.close();
        return address;
    }

    public String addressToContact(String address) {
        String name = address;
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(address));
        Cursor c = getContentResolver().query(uri,
                new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            try {
                if (!(c.getString(c.getColumnIndex(PhoneLookup.DISPLAY_NAME))
                        .equals(""))) {
                    name = c.getString(c
                            .getColumnIndex(PhoneLookup.DISPLAY_NAME));
                }
            } catch (SQLiteException e) {
            }
            Log.d("Name", name);
        }
        c.close();
        return name;
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this); // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this); // Add this method.
    }
}
