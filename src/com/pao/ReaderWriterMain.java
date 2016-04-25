package com.pao;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Silviu on 3/10/16.
 */
public class ReaderWriterMain {

    private static HashMap<String, Integer> wordAppFv = new HashMap<>();
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {

//        populateMap();
//        populateMapFromUrl();
        populateMapFromConnection();
        writeToExtFile();

    }

    public static void populateMapFromConnection() throws IOException {
        String url = "http://stackoverflow.com/questions/1339437/inputstream-or-reader-wrapper-for-progress-reporting";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    public static void populateMapFromUrl() throws IOException {
        URL url = new URL("http://stackoverflow.com/questions/2885173/how-to-create-a-file-and-write-to-a-file-in-java");
        InputStreamReader stream = new InputStreamReader(url.openStream());
        BufferedReader reader = new BufferedReader(stream);
        try {

            String sCurrentLine;

            while ((sCurrentLine = reader.readLine()) != null) {
                String[] tokens = sCurrentLine.split(" ");
                for (String token : tokens) {
                    Integer crtFv = wordAppFv.get(token);
                    if (null == crtFv) {
                        crtFv = 0;
                    }
                    wordAppFv.put(token, ++crtFv);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void writeToExtFile() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("iesire.txt", "UTF-8");

        for (String token : wordAppFv.keySet()) {
            System.out.println(token + ' ' + wordAppFv.get(token));
            writer.println(token + " - " + wordAppFv.get(token));
        }

        writer.close();
    }

    public static void populateMap() {
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/Users/Silviu/TCLab1/src/com/tc/intrare.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                String[] tokens = sCurrentLine.split(" ");
                for (String token : tokens) {
                    Integer crtFv = wordAppFv.get(token);
                    if (null == crtFv) {
                        crtFv = 0;
                    }
                    wordAppFv.put(token, ++crtFv);
                }
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
}


