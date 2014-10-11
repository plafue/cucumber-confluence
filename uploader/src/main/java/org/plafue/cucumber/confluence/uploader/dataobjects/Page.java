package org.plafue.cucumber.confluence.uploader.dataobjects;

import java.util.HashMap;
import java.util.Map;

public abstract class Page {
    private final String type;
    private final Map<String, String> space;
    private final String title;
    private final Body body;

    public Page(String spaceKey, String title, String content){
        this.space = new HashMap<String,String>();
        this.space.put("key", spaceKey);
        this.title = title;
        this.body = new Body(content);
        this.type = "page";
    }

    private class Body {
        Map<String,Map<String,String>> body;

        private Body(String body) {
            Map<String, String> storage = new HashMap<String, String>();
            storage.put("value",body);
            this.body.put("storage", storage);
        }
    }
}
