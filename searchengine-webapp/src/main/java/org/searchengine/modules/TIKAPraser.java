package org.searchengine.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

class Parser implements Runnable {
    
    private File file;
    public Parser(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        //detecting the file type
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream(this.file);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ParseContext pcontext = new ParseContext();
        
        //Html parser 
        HtmlParser htmlparser = new HtmlParser();
        try {
            htmlparser.parse(inputstream, handler, metadata,pcontext);
        } catch (IOException | SAXException | TikaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        TIKAPraser.updateString(handler.toString());
    }
    
    
}
public class TIKAPraser {
    public static void main(String[] args) throws IOException, InterruptedException {
        String inputFolder = "/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment4/HTML_files";
        String outputFile = "/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment5/big.txt";
        File rootDirectory = new File(inputFolder);
        
        ExecutorService service = Executors.newFixedThreadPool(100);
        
        for (File file : rootDirectory.listFiles()) {
            if (file.getName().equals(".DS_Store"))
                continue;
            
            service.submit(new Parser(file));
        }
        
        service.shutdown();
        service.awaitTermination(3, TimeUnit.MINUTES);
        
        FileWriter writer = new FileWriter(new File(outputFile));
        writer.write(TIKAPraser.outputString.toString());
        writer.close();

    }
    public static StringBuilder outputString = new StringBuilder();
    public static int processed = 0;
    
    public synchronized static void updateString(String string) {
        String toWrite = string.trim().replaceAll("[^a-zA-Z0-9\\s\\']+","").replaceAll("[\\s]{2,}"," ").replaceAll("\n", " ");
        outputString.append(toWrite);
        if((processed + 1) % 1 == 0)
            System.out.println("Processed: "+(processed++));
    }
}
