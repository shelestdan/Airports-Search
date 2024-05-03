# A simple text classifier for a chatbot

## Description of the project

This project is a Java console application that implements a simple text classifier for a chatbot. The application receives a CSV file with a list of possible reports and a text request from the user. Then it searches for the most appropriate lines from the file by description and outputs their GUID to the user in the response.

## Using a stemmer

To improve the accuracy of the match search, a stemmer from the library `opennlp.tools.stemmer.snowball' was used.SnowballStemmer`. Stemmer brings words to their base, which allows you to take into account different cases and forms of words when searching for matches.

## Difficulty assessment

The complexity of the algorithm is determined by two main factors: the number of reports in the CSV file and the length of the user's request. In the worst case, when each word in the query matches the description of the report, the complexity of the algorithm is O(n*m), where n is the number of reports and m is the number of words in the query. However, in the average case, when there are fewer matches, the complexity will be much lower.

## Questions and solutions

During the implementation of the project, the question arose about how to take into account the different cases and forms of words when searching for matches. To solve this problem, a stemmer was used, which brings the words to their base. This solution does not contradict the problem statement and allows you to improve the accuracy of the match search. Added handling of encoding-related exceptions.
