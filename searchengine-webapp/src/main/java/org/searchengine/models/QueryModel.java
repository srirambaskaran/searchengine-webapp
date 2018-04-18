package org.searchengine.models;

public class QueryModel {
    private String query;
    private String rankingType;
    
    public QueryModel() { 
        //default constructor for Jackson. 
    }

    public QueryModel(String query, String rankingType) {
        super();
        this.query = query;
        this.rankingType = rankingType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRankingType() {
        return rankingType;
    }

    public void setRankingType(String rankingType) {
        this.rankingType = rankingType;
    }
    
    public void clean() {
        
    }
}
