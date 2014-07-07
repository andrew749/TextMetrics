package com.andrew749.textmetrics;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.analytics.tracking.android.EasyTracker;

import static com.andrew749.textmetrics.MainActivity.SortingTypes.Conversations;
import static com.andrew749.textmetrics.MainActivity.SortingTypes.Recieved;
import static com.andrew749.textmetrics.MainActivity.SortingTypes.Sent;

public class MainActivity extends FragmentActivity {
    final String MYPREFS = "mypreferences";
    String[] optionalmetrics;
    ProgressDialog progress;
    boolean debug = false;
    private DrawerLayout drawerlauout;
    private ListView drawerlist;
    private Bundle mData;
    private SortingTypes state = Conversations;
    private ActionBarDrawerToggle mDrawerToggle;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progress.setMessage(getResources().getString(R.string.loadingmessagelong));
                    break;
            }
        }
    };
    private Thread runningTimer = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(30000);
                handler.sendMessage(Message.obtain(handler, 0, 0, 0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/260506200809325"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/TextMetrics"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(MYPREFS, 0);

        mData = new Bundle();

        optionalmetrics = new String[]{getString(R.string.drawer1), getString(R.string.drawer2), getString(R.string.drawer3), getString(R.string.drawer4), getString(R.string.drawer5)};
        setContentView(R.layout.layoutnew);
        drawerlauout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerlist = (ListView) findViewById(R.id.left_drawer);
        drawerlist.setAdapter(new ArrayAdapter<String>(this, R.layout.drawerlistitem, optionalmetrics));
        drawerlist.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerlauout, R.drawable.ic_drawer, R.string.draweropentext, R.string.app_name);
        drawerlauout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        getData task = new getData(getApplicationContext());
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setMessage(getString(R.string.loadingmessage));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        task.execute();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //place the data appropriately into the fragment and recreate
        mDrawerToggle.onConfigurationChanged(newConfig);

        switch (state) {
            case Conversations:

                break;
            case Recieved:
                break;
            case Sent:
                break;

        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (debug) {
            Log.d("TextMetrics", "Activity Started");
        }
        EasyTracker.getInstance().activityStart(this); // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        if (debug) {
            Log.d("TextMetrics", "Activity Stopped");
        }
        EasyTracker.getInstance().activityStop(this); // Add this method.
    }

    @Override
    protected void onDestroy() {
        handler = null;
        super.onDestroy();
    }

    public void handleTwitter() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=andrewcod749"));
            startActivity(intent);

        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/andrewcod749")));
        }
    }

    public static enum SortingTypes {
        Conversations, Sent, Recieved
    }

    class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }

        /**
         * @param position position 0 is conversations
         *                 1 is sent
         *                 2 is received
         *                 3 is facebook
         *                 4 is twitter
         */
        private void selectItem(int position) {
            Fragment fragment = new Fragment();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

            switch (position) {
                case 0:
                    state = Conversations;

                    fragment = new SpecialFragment();
                    mData.putSerializable("type", state);
                    fragment.setArguments(mData);
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    setTitle(optionalmetrics[position]);

                    break;
                case 1:
                    state = Sent;

                    fragment = new SpecialFragment();
                    mData.putSerializable("type", state);
                    fragment.setArguments(mData);
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    setTitle(optionalmetrics[position]);

                    break;
                case 2:
                    state = Recieved;

                    fragment = new SpecialFragment();
                    mData.putSerializable("type", state);
                    fragment.setArguments(mData);
                    // Insert the fragment by replacing any existing fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    setTitle(optionalmetrics[position]);

                    break;
                case 3:
                    startActivity(getOpenFacebookIntent(getApplicationContext()));
                    break;
                case 4:
                    handleTwitter();
                    break;
            }


            // Highlight the selected item, update the title, and close the drawer
            drawerlist.setItemChecked(position, true);
            drawerlauout.closeDrawer(drawerlist);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void setTitle(CharSequence title) {
            getActionBar().setTitle(title);
        }
    }

    public class getData extends AsyncTask<Void, Void, Data> {
        Context context;
        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

        public getData(Context c) {
            context = c;
        }

        @Override
        protected Data doInBackground(Void... params) {

            Data data = new Data();
            if (debug == true) {
                Log.d("Starting", "Background Thread");
            }
            runningTimer.start();
            Cursor c = context.getContentResolver().query(contentUri, projection,
                    null, null, null);
            c.moveToFirst();

            while (c.moveToNext()) {
                String number = c.getString(c.getColumnIndex(projection[0]));
                String name = c.getString(c
                        .getColumnIndex(projection[1]));
                Contact contact = new Contact(name, number);

                if (!(contact.number == null)) {
                    populateRecievedMessages(contact);
                    populateSentMessages(contact);
                    populateConversations(contact);
                }
                if (debug) {
                    Log.d("Found Contact", contact.name);
                }

                data.addContact(contact);

            }
            if (debug) {
                Log.d("Thread done", "thread");
            }
            c.close();
            runningTimer.interrupt();
            deleteEmptyContacts(data);

            return data;
        }


        @Override
        protected void onPostExecute(Data data) {
            super.onPostExecute(data);
            progress.dismiss();
            mData.putSerializable("data", data);
            mData.putSerializable("type", state);
            Fragment fragment = new SpecialFragment();
            fragment.setArguments(mData);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        //TODO increase efficiency of database queries rather than running twice
        public void populateRecievedMessages(Contact contact) {

            Uri inbox = Uri.parse("content://sms/inbox");
            Cursor c = context.getContentResolver().query(inbox, null,
                    null, null, null);
            if (debug) {
                Log.d("getting messages for ", contact.name);
            }
            c.moveToFirst();
            while (c.moveToNext()) {
                if (contact.number.equals(c.getString(c.getColumnIndex("address")))) {
                    contact.incrementMessagesReceived();
                    if (debug) {
                        Log.d("Recieved " + contact.name, contact.numberOfMessagesRecieved + "");
                    }
                }
            }
            c.close();
        }

        public void deleteEmptyContacts(Data data) {
            for (int i = 0; i < data.contacts.size(); i++) {
                if (data.getContact(i).numberOfMessages == 0) {
                    data.contacts.remove(i);
                }
            }
        }

        public void populateSentMessages(Contact contact) {
            Uri inbox = Uri.parse("content://sms/sent");
            Cursor c = context.getContentResolver().query(inbox, null,
                    null, null, null);
            if (debug) {
                Log.d("getting messages for ", contact.name);
            }
            c.moveToFirst();
            while (c.moveToNext()) {
                if (contact.number.equals(c.getString(c.getColumnIndex("address")))) {
                    contact.incrementMessagesSent();
                    if (debug) {
                        Log.d("sent " + contact.name, contact.numberOfMessagesSent + "");
                    }
                }
            }
            c.close();
        }

        public void populateConversations(Contact contact) {
            contact.numberOfMessages = contact.numberOfMessagesSent + contact.numberOfMessagesRecieved;
            if (debug) {
                Log.d(contact.name, "" + contact.numberOfMessages);
            }
        }


    }

}
