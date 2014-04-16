package com.andrew749.textmetrics;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.concurrent.ExecutionException;

public class MainActivity extends FragmentActivity {
    String[] optionalmetrics = {"Convserations", "Messages Sent", "Messages Recieved", "Twitter"};
    private DrawerLayout drawerlauout;
    private ListView drawerlist;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutnew);
        drawerlauout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerlist = (ListView) findViewById(R.id.left_drawer);
        drawerlist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionalmetrics));
        drawerlist.setOnItemClickListener(new DrawerItemClickListener());
        Fragment fragment=new ConversationsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment);
        getData task = new getData(getApplicationContext());
        task.execute();
        try {
            data=task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public static enum SortingTypes{
        Conversations,Sent,Recieved
    }

    class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }

        /**
         *
         * @param position
         * position 0 is conversations
         * 1 is sent
         * 2 is received
         */
        private void selectItem(int position) {
            Fragment fragment=new Fragment();
            // Create a new fragment and specify the planet to show based on position
            switch(position) {
                case 0:  fragment = new ConversationsFragment();break;
                case 1:
                    fragment=new SpecialFragment(data,SortingTypes.Sent);break;
                case 2:
                    fragment=new SpecialFragment(data,SortingTypes.Recieved);
                    break;
            }

            // Insert the fragment by replacing any existing fragment
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            drawerlist.setItemChecked(position, true);
            setTitle(optionalmetrics[position]);
            drawerlauout.closeDrawer(drawerlist);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void setTitle(CharSequence title) {
            getActionBar().setTitle(title);
        }
    }
}
