package org.searchengine.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class ProcessBigFile {
    public static void main(String[] args) throws IOException {
        HashMap<String, Integer> counts = new HashMap<>();
        InputStream inp = Spelling.class.getResourceAsStream("/big.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        String data = reader.lines().reduce("", new BinaryOperator<String>() {            
            @Override
            public String apply(String t, String u) {
                return t+" "+u;
            }
        });
        reader.close();
        
        Stream.of(data.toLowerCase().replaceAll("[^a-z0-9]","").split(" ")).forEach( (word) ->{
            counts.compute( word, (k,v) -> v == null ? 1 : v + 1  );
        });
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/big-counts.txt"));
        for(String key: counts.keySet()) {
            writer.write(key.replaceAll(",","") +","+counts.get(key));
        }
        writer.close();
        
    }
}
