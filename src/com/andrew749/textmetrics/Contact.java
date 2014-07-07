package com.andrew749.textmetrics;

import java.io.Serializable;

public class Contact implements Serializable {
    public int numberOfMessages = 0;
    public String name, number;

    public long totaltime;
    public int numberOfMessagesSent = 0;
    public int numberOfMessagesRecieved = 0;

    public Contact(String name, String number, int numberOfMessages) {
        this.name = name;
        this.numberOfMessages = numberOfMessages;
        this.number = number;
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public void setTime(long time) {
        this.totaltime = time;
    }

    public void setNumberOfMessagesSent(int messagesSent) {
        this.numberOfMessagesSent = messagesSent;
    }

    public void setNumberOfMessagesReceived(int messagesReceived) {
        this.numberOfMessagesRecieved = messagesReceived;
    }

    public void incrementMessages() {
        numberOfMessages++;
    }

    public void incrementMessagesReceived() {
        numberOfMessagesRecieved++;
    }

    public void incrementMessagesSent() {
        numberOfMessagesSent++;
    }

}
