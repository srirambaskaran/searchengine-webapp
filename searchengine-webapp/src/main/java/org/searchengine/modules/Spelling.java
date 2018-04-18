package org.searchengine.modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Spelling {
    private Map<String,Integer> dict = new HashMap<>();
    private static Spelling spellingObject;
    
    private Spelling() {
        
        try {
            Spelling.spellingObject = new Spelling("/big.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Spelling getInstance() {
        if (Spelling.spellingObject == null)
            try {
                Spelling.spellingObject = new Spelling("/big.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        return Spelling.spellingObject;
    }

    private Spelling(String dictionaryFile) throws Exception{
        InputStream inp = Spelling.class.getResourceAsStream(dictionaryFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp));
        String data = reader.lines().reduce("", new BinaryOperator<String>() {            
            @Override
            public String apply(String t, String u) {
                return t+u;
            }
        });
        reader.close();
        
        Stream.of(data.toLowerCase().replaceAll("[^a-z ]","").split(" ")).forEach( (word) ->{
            dict.compute( word, (k,v) -> v == null ? 1 : v + 1  );
        });
    }

    Stream<String> edits(final String word){
        Stream<String> deletes    = IntStream.range(0, word.length())  .mapToObj((i) -> word.substring(0, i) + word.substring(i + 1));
        Stream<String> replaces   = IntStream.range(0, word.length())  .mapToObj((i)->i).flatMap( (i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj( (c) ->  word.substring(0,i) + (char)c + word.substring(i+1) )  );
        Stream<String> inserts    = IntStream.range(0, word.length()+1).mapToObj((i)->i).flatMap( (i) -> "abcdefghijklmnopqrstuvwxyz".chars().mapToObj( (c) ->  word.substring(0,i) + (char)c + word.substring(i) )  );
        Stream<String> transposes = IntStream.range(0, word.length()-1).mapToObj((i)-> word.substring(0,i) + word.substring(i+1,i+2) + word.charAt(i) + word.substring(i+2) );
        return Stream.of( deletes,replaces,inserts,transposes ).flatMap((x)->x);
    }

    Stream<String> known(Stream<String> words){
        return words.filter( (word) -> dict.containsKey(word) );
    }
    
    public List<String> candidates(String word) {
        List<String> existingWords = known(Stream.of(word)).collect(Collectors.toList());
        if(existingWords != null && existingWords.size() > 0)
            return existingWords;
        List<String> e1 = known(edits(word)).collect(Collectors.toList());
        if(e1 != null && e1.size() > 0)
            return e1;
        
        List<String> e2 = known(edits(word).map( (w2)->edits(w2) ).flatMap((x)->x)).collect(Collectors.toList());
        if(e2 != null && e2.size() > 0)
            return e2;
        
        List<String> noSuggestions = new ArrayList<>();
        noSuggestions.add(word);
        return noSuggestions; 
    }

    public  String correct(String word){
        Optional<String> e1 = known(edits(word)).max( (a,b) -> dict.get(a) - dict.get(b) );
        Optional<String> e2 = known(edits(word).map( (w2)->edits(w2) ).flatMap((x)->x)).max( (a,b) -> dict.get(a) - dict.get(b) );
        return dict.containsKey(word) ? word : ( e1.isPresent() ? e1.get() : (e2.isPresent() ? e2.get() : word));
    }
    
    
    public static void main(String[] args) {
        Spelling spelling = Spelling.getInstance();
        System.out.println(spelling.candidates("donad"));
        System.out.println(spelling.candidates("turnp"));
    }
}