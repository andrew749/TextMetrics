package com.andrew749.textmetrics;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PieChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	ListView view;
	LinearLayout layout;
	static int total_texts;
	public DefaultRenderer defaultRenderer = new DefaultRenderer();
	public GraphicalView chartview;
	// titles
	public String[] contactssample = { "Andrew", "Acod" };
	// distribution
	public double[] distribution = { 10, 90 };
	// colors
	int[] colors = { Color.WHITE, Color.CYAN };
	CategorySeries series = new CategorySeries("Contacts");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Uri uri = Uri.parse("content://sms/inbox");
		view = (ListView) findViewById(R.id.listView1);
		layout = (LinearLayout) findViewById(R.id.chart);
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		startManagingCursor(c);
		String[] body = new String[c.getCount()];
		String[] number = new String[c.getCount()];

		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				body[i] = c.getString(c.getColumnIndexOrThrow("body"))
						.toString();
				number[i] = c.getString(c.getColumnIndexOrThrow("address"))
						.toString();
				c.moveToNext();
			}
		}
		total_texts = c.getCount();
		c.close();
		ArrayAdapter a = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, number);

		for (int i = 0; i < distribution.length; i++) {
			series.add(contactssample[i], distribution[i]);
		}
		for (int i = 0; i < distribution.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
		defaultRenderer.setLegendTextSize(30);
		defaultRenderer.setChartTitle("Mobile Platforms");
		defaultRenderer.setChartTitleTextSize(20);
		defaultRenderer.setZoomButtonsVisible(true);
		defaultRenderer.setBackgroundColor(45454545);
		chartview = ChartFactory.getPieChartView(this, series, defaultRenderer);
		layout.addView(chartview);
		view.setAdapter(a);

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

}
