package com.andrew749.textmetrics;

public class Conversations {
    public String address = "";
    public int messages = 0;

    public Conversations(String address, int number) {
        this.address = address;
        messages = number;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return address;
    }
}
