package com.andrew749.textmetrics;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by andrew on 16/04/14.
 */
public class SpecialFragment extends Fragment {
    public DefaultRenderer defaultRenderer = new DefaultRenderer();
    public GraphicalView chartview;
    // colors
    public int[] color = {Color.GREEN, Color.RED, Color.BLACK, Color.BLUE,
            Color.RED, Color.BLACK, Color.YELLOW};
    Data results;
    ListView lview;
    LinearLayout layout;
    statsadapter a;
    Activity activity;
    CategorySeries series = new CategorySeries("Contacts");
    MainActivity.SortingTypes types;

    public SpecialFragment(Data data, MainActivity.SortingTypes type) {
        results = data;
        types = type;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;


    }

    public void setupRenderer() {
        defaultRenderer.setLegendTextSize(30);
        defaultRenderer.setChartTitle(getString(R.string.charttitle));
        defaultRenderer.setLabelsTextSize(30f);
        defaultRenderer.setChartTitleTextSize(20);
        defaultRenderer.setLabelsColor(Color.BLACK);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setBackgroundColor(45454545);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        lview = (ListView) view.findViewById(R.id.listView1);
        layout = (LinearLayout) view.findViewById(R.id.chart);

        series.clear();

        for (int i = 0; i < results.contacts.size(); i++) {
            switch (types) {
                case Sent:
                    series.add(results.getContact(i).name, results.getContact(i).numberOfMessagesSent);
                    break;
                case Recieved:
                    series.add(results.getContact(i).name, results.getContact(i).numberOfMessagesRecieved);
                    break;
            }
        }

        Random rn = new Random();
        for (int i = 0; i < series.getItemCount(); i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            // seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            seriesRenderer.setColor(Color.argb(255, rn.nextInt(256),
                    rn.nextInt(256), rn.nextInt(256)));
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        setupRenderer();
        chartview = ChartFactory.getPieChartView(getActivity().getApplicationContext(), series, defaultRenderer);
        layout.addView(chartview);

        a = new statsadapter(getActivity().getApplicationContext(), results, types);
        lview.setAdapter(a);
        chartview.repaint();
        AdView layout = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        layout.loadAd(adRequest);
        return view;
    }
}
