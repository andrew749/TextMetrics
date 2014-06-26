package com.andrew749.textmetrics;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
	public ArrayList<Contact> contacts = new ArrayList<Contact>();

	public Data() {

	}

	public void addContact(Contact c) {
		contacts.add(c);
	}
public void removeContact(int index){contacts.remove(index);}
	public Contact getContact(int i) {
		return contacts.get(i);
	}
}
