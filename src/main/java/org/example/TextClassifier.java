package org.example;


import java.util.List;

public class TextClassifier {
    private final Classifier classifier;

    public TextClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public List<String> classify(String query) {
        return classifier.classify(query);
    }
}
