package com.xmacedo.MyOwnDontPad;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private  Map<String, Document> database = new HashMap<>();

    public  Map<String, Document> getDatabase() {
        return database;
    }

    public void setDatabase(HashMap<String, Document> database) {
        database = database;
    }
}
