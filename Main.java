import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String filename = "test.txt";
        try {
            LexicalAnalyzer analyzer = new LexicalAnalyzer(filename);
            List<Lexeme> lexemes = analyzer.analyze();

            System.out.println("Success!");
            System.out.println("Number\t\tLexeme\t\tType");

            int index = 1;
            for (Lexeme lex : lexemes) {
                System.out.println(index + ":\t\t" + lex.getValue() + "\t\t" + lex.getType());
                index++;
            }

            BinarySearchTree variables = analyzer.getVariables();
            System.out.println("Variables:");
            variables.print();
        } catch (IOException e) {
            System.out.println("Error opening file!");
        } catch (LexicalException e) {
            System.out.println("Lexical error: " + e.getMessage());
        }
    }
}
