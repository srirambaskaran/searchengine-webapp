package org.searchengine.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SampleAFile {
    public static void main(String[] args) throws IOException {
        String inputFile = "/Users/srirambaskaran/Documents/Classes/InformationRetrieval/Assignments/Assignment5/big.txt";
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        char[] chbuf = new char[1024];
        int i = 0;
        while((i++) <= 1000) {
            reader.read(chbuf);
            System.out.println(new String(chbuf));
        }
        reader.close();
    }
}
