package com.andrew749.textmetrics;

import java.util.ArrayList;

public class Data {
	public ArrayList<Contact> contacts = new ArrayList<Contact>();

	public Data() {

	}

	public void addContact(Contact c) {
		contacts.add(c);
	}

	public Contact getContact(int i) {
		return contacts.get(i);
	}
}
