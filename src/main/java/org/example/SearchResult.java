package org.example;

import java.util.List;

record SearchResult(String query, List<String> matches) {
}