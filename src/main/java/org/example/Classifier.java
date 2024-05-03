package org.example;

import java.util.List;

public interface Classifier {
    List<String> classify(String query);
}
