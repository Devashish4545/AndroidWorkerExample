package com.xrstudio.rxjava.demoappofworkmanager.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MST_MESSAGE_TABLE extends RealmObject {

    @PrimaryKey
    private String stanzaID;
    private String message;
    private int messageDirection;
    private long messageDate;

    public MST_MESSAGE_TABLE() {

    }

    public MST_MESSAGE_TABLE(String stanzaID, String message, int messageDirection, long messageDate) {
        this.stanzaID = stanzaID;
        this.message = message;
        this.messageDirection = messageDirection;
        this.messageDate = messageDate;
    }

    public String getStanzaID() {
        return stanzaID;
    }

    public void setStanzaID(String stanzaID) {
        this.stanzaID = stanzaID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(int messageDirection) {
        this.messageDirection = messageDirection;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(long messageDate) {
        this.messageDate = messageDate;
    }
}
