package com.xmacedo.MyOwnDontPad.model;

public class Document {
    private String uuid;
    private String text;

    public Document(String uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
