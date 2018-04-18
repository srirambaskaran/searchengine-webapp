package org.searchengine.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SearchEngineParams {
    
    private static SearchEngineParams parameters;
    private String solrUrl;
    private String collection;
    
    private SearchEngineParams(String solrUrl, String collection) {
        this.solrUrl = solrUrl;
        this.collection = collection;
    }
    
    public static SearchEngineParams getInstance() {
        if(SearchEngineParams.parameters == null) {
            parameters = createInstance();
        }
        return parameters;
    }
    
    public String getSolrUrl() {
        return this.solrUrl;
    }

    private static SearchEngineParams createInstance() {
        return new SearchEngineParams("http://localhost:8983/solr","foxnews");
    }

    public String getCollection() {
        return this.collection;
    }

    public static HashMap<String, String> getMappings() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(SearchEngineParams.class.getResourceAsStream("/UrlToHtml_foxnews.csv")));
        HashMap<String, String> mappings = new HashMap<>();
        String line = "";
        try {
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                mappings.put(tokens[0].trim(), tokens[1].trim());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mappings;
    }
}
