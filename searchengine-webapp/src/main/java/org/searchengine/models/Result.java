package org.searchengine.models;

public class Result {
    
    private String link;
    private String title;
    private String para;
    private String id;
    
    public Result() {
        super();
    }

    public Result(String link, String title, String para) {
        this.link = link;
        this.title = title;
        this.para = para;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getPara() {
        return para;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
