package org.example;

import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubstringClassifier implements Classifier {
    private final Map<String, String> reports;
    private final SnowballStemmer stemmer;

    public SubstringClassifier(Map<String, String> reports) {
        this.reports = reports;
        this.stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.RUSSIAN);
    }

    @Override
    public List<String> classify(String query) {
        String[] queryWords = query.split("\\s+");
        List<String> stemmedQueryWords = Arrays.stream(queryWords)
                .map(this::stem)
                .toList();

        return reports.entrySet().stream()
                .filter(entry -> stemmedQueryWords.stream().anyMatch(stemmedWord -> stem(entry.getKey()).contains(stemmedWord)))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private String stem(String text) {
        return Arrays.stream(text.split("\\s+"))
                .map(stemmer::stem)
                .map(CharSequence::toString)
                .collect(Collectors.joining(" "));
    }
}
