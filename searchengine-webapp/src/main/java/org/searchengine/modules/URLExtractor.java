package org.searchengine.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;

class Edge{
    private String url1;
    private String url2;
    public Edge(String url1, String url2) {
        super();
        this.url1 = url1;
        this.url2 = url2;
    }
    @Override
    public int hashCode() {
        final int prime1 = 31;
        final int prime2 = 37;
        int result = 1;
        result = prime1 * result + ((url1 == null) ? 0 : url1.hashCode());
        result = prime2 * result + ((url2 == null) ? 0 : url2.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        if (url1 == null) {
            if (other.url1 != null)
                return false;
        } else if (!url1.equals(other.url1))
            return false;
        if (url2 == null) {
            if (other.url2 != null)
                return false;
        } else if (!url2.equals(other.url2))
            return false;
        return true;
    }
}

public class URLExtractor {
    public static void main (String[] args) throws IOException {
        File mappingFile = new File("/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment4/UrlToHtml_foxnews.csv");
        HashMap<String, String> mappings = readMappingFile(mappingFile);
        HashMap<String, String> reverseMappings = reverse(mappings);
        Set<String> urls = new HashSet<>(mappings.values());
        File rootDirectory = new File("/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment4/HTML_files/");
        File[] htmlFiles = rootDirectory.listFiles();
        CSVWriter writer = new CSVWriter(new FileWriter("/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment4/outgoingLinks.csv"));
        
        int i = 0;
        for(File htmlFile : htmlFiles) {
            Document doc = Jsoup.parse(htmlFile, "UTF-8", "http://www.foxnews.com/");
            Elements links = doc.select("a[href]");
            i+=1;
            for(Element link : links) {
               String linkStr = link.attr("abs:href");
               
               if(urls.contains(linkStr)) {
                   String[] nextLine = new String[2];
                   nextLine[0] = reverseMappings.get(mappings.get(htmlFile.getName()));
                   nextLine[1] = reverseMappings.get(linkStr);
                   writer.writeNext(nextLine);
                   
               }
            }
            
            if(i%100 == 0)
                System.out.println("Processed "+i+" files.");
        }
        System.out.println(i);
        writer.close();
        
    }

    private static HashMap<String, String> reverse(HashMap<String, String> mappings) {
        HashMap<String, String> reverseMappings = new HashMap<>();
        for(String id: mappings.keySet()) {
            if(!reverseMappings.containsKey(mappings.get(id)))
                reverseMappings.put(mappings.get(id), "/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment4/HTML_files/"+id);
        }
        return reverseMappings;
    }

    private static HashMap<String, String> readMappingFile(File mappingFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(mappingFile));
        HashMap<String, String> mappings = new HashMap<>();
        String line = "";
        while((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            mappings.put(tokens[0].trim(), tokens[1].trim());
        }
        reader.close();
        return mappings;
    }
}
