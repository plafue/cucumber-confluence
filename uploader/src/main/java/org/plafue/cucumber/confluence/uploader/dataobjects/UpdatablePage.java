package org.plafue.cucumber.confluence.uploader.dataobjects;

import java.util.HashMap;
import java.util.Map;

public class UpdatablePage extends Page {
    public final String id;
    public final Map<String, Integer> version;

    public UpdatablePage(String spaceKey, String title, String content, String id, Integer version) {
        super(spaceKey, title, content);
        this.id = id;
        this.version = new HashMap<String, Integer>();
        this.version.put("number", version);
    }
}
