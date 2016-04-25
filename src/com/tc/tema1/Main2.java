package com.tc.tema1;

import com.tc.tema1.enums.Keywords;
import com.tc.tema1.enums.Separators;
import com.tc.tema1.enums.Tokens;
import com.tc.tema1.enums.operators.*;
import com.tc.tema1.object.TokenObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Silviu on 3/13/16.
 */
public class Main2 {

    /**
     * The property is global so that all the methods of this class
     * will access the same file reader stream
     */
    private static RandomAccessFile file = null;
    private static long crtFileLine = 1;


    public static void main(String[] args) throws IOException {
        openFile();
        while (!reachedEndOfFile()) {
            adjustFilePointer();
            TokenObject token = buildToken();
            parseReturnedToken(token);
        }
        closeFile();
    }

    /**
     * Builds the token object
     * Manages special situations (comment tokens, import tokens)
     *
     * @return TokenObject
     * @throws IOException
     */
    public static TokenObject buildToken() throws IOException {

        TokenObject token = null;
        String word = "";

        char crtChar = (char) file.read();

        //try to build a word
        while (noSpacesOrNewLine(crtChar)) {

            word += crtChar;

//            if(crtChar == ' ' || crtChar == '\n') {
            token = parseResult(word, token);
//            }

            //exit only if i've read full lines, not words
            if (isCommentOrImport(word, token)) {
                break;
            }

            crtChar = (char) file.read();

            if (crtChar == '\n') {
                crtFileLine++;
            }
        }
        return token;
    }

    public static boolean isCommentOrImport(String word, TokenObject token) {
        return token != null && word.length() != token.getValue().length();
    }

    public static boolean noSpacesOrNewLine(char crtChar) {
        return crtChar != ' ' && crtChar != '\n';
    }

    public static TokenObject parseResult(String result, TokenObject token) throws IOException {
        boolean prevDone = false;
        if (isComment(result)) {
            token = createToken(Tokens.COMMENT, result);
            result = getCommentTokenAsString(result);
            token.setTokenLength(result.length());
            token.setValue(result);
            prevDone = true;
        } else if (isImport(result)) {
            result += readWithUpdateFromFile();
            token = createToken(Tokens.KEYWORD, result);
            prevDone = true;
        } else if (isKeyword(result)) {
            token = createToken(Tokens.KEYWORD, result);
            prevDone = true;
        } else if (isOperator(result)) {
            token = createToken(Tokens.OPERATOR, result);
//            result = replaceOperator(result);
            prevDone = true;
        } else if (isSeparator(result)) {
            token = createToken(Tokens.SEPARATOR, result);
            result = replaceSeparator(result);
            token.setValue(result);
            prevDone = true;
        }
        if (!prevDone) {
//            System.out.println(result);
        }
        return token;
    }

    public static String deleteUnwantedOccurences(String result, Enum e) {
        for(int i = 0; i < result.length(); ++ i) {
            String c = Character.toString(result.charAt(i));
            if(!c.equals(e.toString())) {
                result = result.replace(c," ");
            }
        }
//        if(result.contains(e.toString())) {
//            return e.toString();
//        }

        return result;
    }

    public static String replaceSeparator(String result) {
        for (Enum e : Separators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        return result.trim();
    }

    public static String replaceOperator(String result) {
        for (Enum e : ArithmeticOperations.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        for (Enum e : AssignmentOperators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        for (Enum e : BitwiseOperators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        for (Enum e : LogicalOperators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        for (Enum e : RelationalOperators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        for (Enum e : SpecialOperators.values()) {
            if (result.contains(e.toString())) {
                result = deleteUnwantedOccurences(result, e);
            }
        }
        return result.trim();
    }

    public static boolean isSeparator(String result) {
        for (Enum e : Separators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOperator(String result) {
        for (Enum e : ArithmeticOperations.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        for (Enum e : AssignmentOperators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        for (Enum e : BitwiseOperators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        for (Enum e : LogicalOperators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        for (Enum e : RelationalOperators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }
        for (Enum e : SpecialOperators.values()) {
            if (result.contains(e.toString())) {
                return true;
            }
        }

        return false;
    }

    public static TokenObject createToken(Enum e, String result) throws IOException {
        return new TokenObject(e, result.length(), crtFileLine, file.getFilePointer(), "", result);
    }

    public static String readWithUpdateFromFile() throws IOException {
        crtFileLine++;
        return file.readLine();

    }

    public static boolean isKeyword(String result) {
        for (Enum e : Keywords.values()) {
            if (e.toString().toLowerCase().equals(result)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isComment(String result) {
        return result.length() >= 2 && (result.substring(0, 2).equals("//") || result.equals("/*")) || result.length() >= 3 && result.substring(0, 3).equals("/**");
    }

    public static boolean isImport(String result) {
        return result.equals(Keywords.IMPORT.toString().toLowerCase());
    }

    public static String getCommentTokenAsString(String result) throws IOException {
        if (result.length() >= 2 && result.substring(0, 2).equals("//")) {
            result += readWithUpdateFromFile();
        } else if (result.length() >= 2 && result.substring(0, 2).equals("/*") || result.length() >= 3 && result.substring(0, 3).equals("/**")) {
            result += readWithUpdateFromFile().replace("\\s+$", "");
            //clear final whitespaces
            while (result.length() >= 2 && !result.substring(result.length() - 2).equals("*/")) {
                result += " " + readWithUpdateFromFile();
            }
        }
        return result;
    }

    /**
     * Displays the info from <code>returnValue</code>
     * as parsed info on stdout
     *
     * @param returnValue
     */
    public static void parseReturnedToken(TokenObject returnValue) {
        if (returnValue != null) {
            System.out.print("CURRENT LINE:" + returnValue.getCrtFileLine() + " ---- ");
            System.out.print("ERROR MSG: " + returnValue.getErrorMsg() + " ---- ");
            System.out.print("TOKEN LENGTH: " + returnValue.getTokenLength() + " ---- ");
            System.out.print("TOKEN POINTER: " + returnValue.getTokenPointer() + " ---- ");
            System.out.print("TOKEN TYPE: " + returnValue.getTokenType().toString() + " ---- ");
            System.out.print("TOKEN VALUE: " + returnValue.getValue().replace("\\r|\\n", ""));
            System.out.println();
        }
    }

    /**
     * It adjusts the file pointer on the last blank space encountered.
     * The reason is so that when <code>file.read()</code> is called,
     * the result is the first letter of the word
     *
     * @throws IOException
     */
    public static void adjustFilePointer() throws IOException {
        char crtChar = (char) file.read();
        while (crtChar == ' ' || crtChar == '\n') {
            if (crtChar == '\n') crtFileLine++;
            crtChar = (char) file.read();
        }
        file.seek(file.getFilePointer() - 1);
    }

    public static void openFile() throws FileNotFoundException {
        file = new RandomAccessFile(new File("/Users/Silviu/TCLab1/src/com/tc/files/intrare.txt"), "r");
    }

    public static void closeFile() throws IOException {
        file.close();
    }

    public static boolean reachedEndOfFile() throws IOException {
        return file.getFilePointer() == file.length();
    }

}
