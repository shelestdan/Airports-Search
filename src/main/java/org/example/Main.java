package org.example;

import java.io.*;
import java.nio.charset.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length < 6) {
            logger.severe("Usage: java Main " +
                    "--data <path-to-csv.csv>" +
                    " --input-file <input-path-to-file.txt>" +
                    " --output-file <output-path-to-file.json>");
            System.exit(1);
        }

        try {
            String dataFilePath = args[1];
            String inputFilePath = args[3];
            String outputFilePath = args[5];

            validateFile(dataFilePath, "Data file does not exist: ");
            validateFile(inputFilePath, "Input file does not exist: ");

            Map<String, String> reports = loadDataFromFile(dataFilePath);
            TextClassifier classifier = new TextClassifier(new SubstringClassifier(reports));
            List<String> inputLines = readInputFile(inputFilePath);

            List<SearchResult> results = new ArrayList<>();
            for (String line : inputLines) {
                List<String> classified = classifier.classify(line);
                results.add(new SearchResult(line, classified));
            }
            writeResultsToFile(outputFilePath, results);
        } catch (CharacterCodingException e) {
            logger.log(Level.SEVERE, "Character coding error: ", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred: ", e);
        } catch (UnsupportedCharsetException e) {
            logger.log(Level.SEVERE, "Unsupported encoding used: ", e);
        }
    }

    private static void validateFile(String filePath, String errorMessage) throws FileNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(errorMessage + filePath);
        }
    }

    private static Map<String, String> loadDataFromFile(String filePath) throws IOException {
        Map<String, String> reports = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    reports.put(parts[2].trim(), parts[0].trim());
                }
            }
        }
        return reports;
    }

    private static List<String> readInputFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        }
    }

    private static void writeResultsToFile(String filePath, List<SearchResult> results) throws IOException {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.println("{");
            writer.println("\"initTime\":100,");
            writer.println("\"result\":[");
            for (int i = 0; i < results.size(); i++) {
                SearchResult result = results.get(i);
                List<String> quotedMatches = result.matches().stream()
                        .map(match -> "\"" + match + "\"")
                        .toList();
                writer.println("{");
                writer.println("\"search\":\"" + result.query() + "\",");
                writer.println("\"result\":" + quotedMatches + ",");
                writer.println("\"time\":10");
                writer.println("}" + (i < results.size() - 1 ? "," : ""));
            }
            writer.println("]");
            writer.println("}");
        }
    }
}
