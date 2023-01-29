package Code;

import Code.lexical.LexicalAnalyzer;
import Code.lexical.Token;
import Code.lexical.enums;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import Code.lexical.enums.TokenType;

public class Main {
    public static void main(String[] args) {
        File inputFile = new File("src/Code/lexpositivegrading.src");
        File outputFile = new File("src/Code/output.txt");
        // create a new instance of the LexicalScanner class
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(inputFile);

        // create a new PrintWriter to write to the output file
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(outputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // call the nextToken() method as many times as possible
        Token token;
        while ((token = lexicalAnalyzer.nextToken()) != null && token.getType() != TokenType.EOF) {
            // write the token to the output file
            writer.println(token);
        }

        // close the PrintWriter
        writer.close();
    }
}