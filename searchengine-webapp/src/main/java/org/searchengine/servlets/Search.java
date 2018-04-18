package org.searchengine.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.searchengine.exceptions.SearchEngineException;
import org.searchengine.models.QueryModel;
import org.searchengine.models.Result;
import org.searchengine.models.ResultModel;
import org.searchengine.models.SearchEngineParams;

public class Search {
    private QueryModel queryModel;
    
    private static HttpSolrClient client;
    private static HashMap<String, String> mappings = SearchEngineParams.getMappings();
    
    public Search(QueryModel queryModel) {
        this.queryModel = queryModel;
        client = null;
    }
    
    public ResultModel run() throws SearchEngineException {
        HttpSolrClient client = getOrCreateClient();
        Map<String, String> queryParamMap = new HashMap<String, String>();
        
        queryParamMap.put("q", queryModel.getQuery());
        if(queryModel.getRankingType().equals("pagerank")) {
            queryParamMap.put("sort", "pageRankFile desc");
        }
        queryParamMap.put("rows","10");
        //set parameters for page rank
        
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        QueryResponse response = null;
        try {
            response = client.query(SearchEngineParams.getInstance().getCollection(), queryParams);
        } catch (SolrServerException | IOException e) {
            throw new SearchEngineException("Issue with searching");
        }
        SolrDocumentList documents = response.getResults();
        
        ResultModel resultModel = new ResultModel();
        resultModel.setNumResult((int)documents.getNumFound());
        resultModel.setStatus(true);
        resultModel.setMessage("success");
        for(SolrDocument document: documents) {
            Object desc = document.getFieldValue("og_description");
            String[] idTokens = document.getFieldValue("id").toString().split("/");
            String id = idTokens[idTokens.length-1];
            String para = (desc == null ? "NA" :createStringFromArrayList(desc)) +" ID:" +id;
            String link = createStringFromArrayList(document.getFieldValue("og_url"));
            if(link.equals("")) {
                link = mappings.get(id);
            }
            String title = createStringFromArrayList(document.getFieldValue("og_title"));
            if(title.equals("")) {
                title = mappings.get(id);
            }
            Result result = new Result(link, title, para);
            result.setId(id);
            resultModel.addResult(result);
        }

        return resultModel;
    }
    
    @SuppressWarnings("unchecked")
    private String createStringFromArrayList(Object obj) {
        if(obj == null)
            return "";
        else if (obj instanceof ArrayList)
            return ((ArrayList<String>)obj).get(0);
        else return "";
    }
    
    private HttpSolrClient getOrCreateClient() {
        if(client == null) {
            final String solrUrl = SearchEngineParams.getInstance().getSolrUrl();
            return new HttpSolrClient.Builder(solrUrl)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
        }
        return client;
        
    }
}
