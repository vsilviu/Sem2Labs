package com.tc.tema1;

import com.tc.tema1.enums.*;
import com.tc.tema1.enums.operators.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Silviu
 *         For reference I used https://www.cs.cmu.edu/~pattis/15-1XX/15-200/lectures/tokens/lecture.html
 */
public class Main {

    private static BufferedReader br = null;

    public static void main(String[] args) {
        //read file
        //parse file
        //close file

        //i need a static fct here which returns an object
        readFile();
    }

    public static void readFile() {

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/Users/Silviu/TCLab1/src/com/tc/files/intrare.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                parseCurrentLine(sCurrentLine);
//                System.out.println(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void parseCurrentLine(String crtLine) throws IOException {
        //check imports
//        if (crtLine.contains(toLowercaseString(Keywords.IMPORT))) {
//            System.out.println(Tokens.KEYWORD + " " + toLowercaseString(Keywords.IMPORT));
//            System.out.println(Tokens.LITERAL + " " + getRestOfLine(Keywords.IMPORT, crtLine));
//        }
        //check comments
//        if (crtLine.contains(toLowercaseString(Comments.ONE_LINE)) && isNotStringLiteral(Comments.ONE_LINE, crtLine)) {
//            System.out.println(Tokens.COMMENT + " " + crtLine);
//        }
//        if (crtLine.contains(toLowercaseString(Comments.MULTI_LINE_DOC_LEFT))) {
//            System.out.print(Tokens.COMMENT + " ");
//            do {
//                System.out.print(crtLine + " ");
//                crtLine = br.readLine();
//            } while(crtLine != null && !crtLine.contains(toLowercaseString(Comments.MULTI_LINE_DOC_RIGHT)));
//            System.out.println(Comments.MULTI_LINE_DOC_RIGHT);
//        }
        //check keywords
        for (Enum e : Keywords.values()) {
            assert crtLine != null;
            if (crtLine.contains(toLowercaseString(e))) {
                if (e != Keywords.IMPORT) {
                    System.out.println(Tokens.KEYWORD + " " + toLowercaseString(e));
                    crtLine = crtLine.replace(toLowercaseString(e), "");
                } else {
                    System.out.println(Tokens.KEYWORD + " " + crtLine);
                    crtLine = "";
                }
            }
        }
        for (Enum e : Comments.values()) {
            assert crtLine != null;
            if (crtLine.contains(toLowercaseString(e)) && isNotStringLiteral(e, crtLine)) {
                System.out.println(Tokens.COMMENT + " " + crtLine);
                crtLine = "";
            }
        }
        for (Enum e : Separators.values()) {
            assert crtLine != null;
            if (crtLine.contains(toLowercaseString(e))) {
                System.out.println(Tokens.SEPARATOR + " " + toLowercaseString(e));
                crtLine = crtLine.replace(toLowercaseString(e), "");
            }
        }
        for (Enum e : ArithmeticOperations.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : AssignmentOperators.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : BitwiseOperators.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : LogicalOperators.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : RelationalOperators.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : SpecialOperators.values()) {
            displayToken(crtLine, e);
        }
        for (Enum e : Types.values()) {
            assert crtLine != null;
            if (crtLine.contains(toLowercaseString(e))) {
                System.out.println(Tokens.TYPE + " " + toLowercaseString(e));
                crtLine = crtLine.replace(toLowercaseString(e), "");
            }
        }
    }

    public static String toLowercaseString(Enum s) {
        return s.toString().toLowerCase();
    }

    public static String getRestOfLine(Enum e, String line) {
        return line.substring(e.toString().length() + 1);
    }

    public static boolean isNotStringLiteral(Enum e, String line) {
        return !line.split("//")[0].contains("\"");
    }

    public static void displayToken(String crtLine, Enum e) {
        assert crtLine != null;
        if (crtLine.contains(toLowercaseString(e))) {
            System.out.println(Tokens.OPERATOR + " " + toLowercaseString(e));
            crtLine = crtLine.replace(toLowercaseString(e), "");
        }
    }

}
