package org.searchengine.unittests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.searchengine.exceptions.SearchEngineException;
import org.searchengine.models.QueryModel;
import org.searchengine.models.Result;
import org.searchengine.servlets.Search;

public class SearchAPITest {
    
    public static void main(String[] args) throws SearchEngineException {
        String[] queries = {
                "Bitcoin Blockchain",
                "Stephen Hawking",
                "Falcon Heavy",
                "Syrian War",
                "LA Rams",
                "Black Panther",
                "Austin Bombings",
                "Paris Climate Deal"  
        };
        
        
        for (String query: queries) {
            System.out.println("#### Query: "+query);
//            System.out.println("\n##### Lucene\n");
            System.out.println("\n|Lucene Results|Page Rank Results|");
            System.out.println("|-|-|-|-|-|");
            Search lucene = new Search(new QueryModel(query,"lucene"));
            Search pageRank = new Search(new QueryModel(query, "pagerank"));
            List<Result> luceneResults = lucene.run().getResponses();
            List<Result> pagerankResults = pageRank.run().getResponses();
            Set<String> luceneIds = getIds(luceneResults);
            for(int i=0; i<luceneResults.size();i++) {
                System.out.println("|" + luceneResults.get(i).getLink()+ "|" + pagerankResults.get(i).getLink() +"|");
            }
//            System.out.println("\n##### Page Rank\n");
//            System.out.println("|URL|ID|");
//            System.out.println("|-|-|-|-|-|");
//            for(Result res : pagerankResults) {
//                System.out.println("|"+res.getLink() + "|" + res.getId() + "|");
//            }
            int max = luceneResults.size();
            String overlap = "";
            for (int i=0; i<max ; i++) {
                overlap += luceneIds.contains(pagerankResults.get(i).getId()) ? pagerankResults.get(i).getId()+"\n" : "";
            }
            
            if (overlap.equals(""))
                System.out.println("\n\nOverlap: None");
            else
                System.out.println("\n\nOverlap: \n"+overlap);
            
            System.out.println("\n");

        }
        
        
        
    }

    private static Set<String> getIds(List<Result> results) {
        Set<String> ids = new HashSet<>();
        for (Result result : results)
            ids.add(result.getId());
        return ids;
    }
}
