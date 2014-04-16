package com.andrew749.textmetrics;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

public class getData extends AsyncTask<Void, Void, Data> {
    Context context;

    public getData(Context c) {
        context = c;
    }

    Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    String[] projection = {ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

    @Override
    protected Data doInBackground(Void... params) {
        Data data = new Data();
        Log.d("Starting", "Background Thread");
        Cursor c = context.getContentResolver().query(contentUri, projection,
                null, null, null);
        c.moveToFirst();
        while (c.moveToNext()) {
            String number = c.getString(c.getColumnIndex(projection[0]));
            String name = c.getString(c
                    .getColumnIndex(projection[1]));
            Contact contact = new Contact(name, number);
                    populateRecievedMessages(contact);

                    populateSentMessages(contact);

            Log.d("Found Contact", contact.name);
            data.addContact(contact);

        }
        Log.d("Thread done", "thread");
        c.close();
        return data;
    }

    public void populateRecievedMessages(Contact contact) {
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor c = context.getContentResolver().query(inbox, null,
                null, null, null);
        Log.d("getting messages for ", contact.name);
        c.moveToFirst();
        while (c.moveToNext()) {
            if (contact.number.equals(c.getString(c.getColumnIndex("address")))) {
                contact.incrementMessagesReceived();
                Log.d("Recieved " + contact.name, contact.numberOfMessagesRecieved + "");
            }
        }
    }

    public void populateSentMessages(Contact contact) {
        Uri inbox = Uri.parse("content://sms/sent");
        Cursor c = context.getContentResolver().query(inbox, null,
                null, null, null);
        Log.d("getting messages for ", contact.name);
        c.moveToFirst();
        while (c.moveToNext()) {
            if (contact.number.equals(c.getString(c.getColumnIndex("address")))) {
                contact.incrementMessagesSent();
                Log.d("sent " + contact.name, contact.numberOfMessagesSent + "");
            }
        }
    }
}
