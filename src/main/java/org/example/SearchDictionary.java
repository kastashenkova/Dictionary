package org.example;

import java.text.Collator;
import java.util.Locale;

import java.util.*;

/*Особливості реалізації:
— слова зберігаються з урахуванням регістру;
— під час видалення слова очищуються порожні гілки дерева;
— додавання слова: O(m), де m - довжина слова;
— видалення слова: O(m);
— перевірка наявності: O(m);
— пошук із джокером: O(m + k log k), де m - довжина префікса,
    k - кількість результатів (log k - через сортування в getAllWords);
— підрахунок слів: O(1)*/

public class SearchDictionary {

    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEndOfWord;

        TrieNode() {
            children = new HashMap<>();
            isEndOfWord = false;
        }
    }

    private final TrieNode root;
    private int counter;
    private final Collator ukrainianCollator;

    public SearchDictionary() {
        root = new TrieNode();
        counter = 0;
        ukrainianCollator = Collator.getInstance(new Locale("uk", "UA"));
    }

    public void addWord(String word) {
        addWord(word, false);
    }

    public void addWord(String word, boolean doNotWriteAboutAddingAWord) {
        String processedWord = checkAndTrim(word);
        if (processedWord == null) {
            if (!doNotWriteAboutAddingAWord) System.out.println("Invalid word");
            return;
        }

        TrieNode targetNode = navigateToWordEnd(processedWord);

        if (targetNode.isEndOfWord) {
            if (!doNotWriteAboutAddingAWord) System.out.println("The word already exists");
            return;
        }

        markWord(targetNode);
        if (!doNotWriteAboutAddingAWord) System.out.println("The new word added into the dictionary");
    }

    public String delWord(String word) {
        String processedWord = checkAndTrim(word);
        if (processedWord == null) {
            return null;
        }

        TrieNode targetNode = findNodeByPath(processedWord);
        if (targetNode == null || !targetNode.isEndOfWord) {
            return null;
        }

        unmarkWord(targetNode);
        cleanEmptyNodes(root, processedWord, 0);

        return processedWord;
    }

    // recursive helper
    private boolean cleanEmptyNodes(TrieNode currentNode, String word, int position) {
        if (position == word.length()) {
            return currentNode.children.isEmpty();
        }

        char symbol = word.charAt(position);
        TrieNode childNode = currentNode.children.get(symbol);

        if (childNode == null) {
            return false;
        }

        boolean canRemoveChild = cleanEmptyNodes(childNode, word, position + 1);

        if (canRemoveChild) {
            currentNode.children.remove(symbol);
            return currentNode.children.isEmpty() && !currentNode.isEndOfWord;
        }

        return false;
    }

    public boolean hasWord(String word) {
        String processedWord = checkAndTrim(word);
        if (processedWord == null) {
            return false;
        }

        TrieNode targetNode = findNodeByPath(processedWord);
        return targetNode != null && targetNode.isEndOfWord;
    }

    public Iterable<String> query(String query) {
        List<String> matchedWords = new ArrayList<>();

        String processedQuery = checkAndTrim(query);
        if (processedQuery == null) {
            return matchedWords;
        }

        if (hasAsterisk(processedQuery)) {
            jokerSearch(processedQuery, matchedWords);
        } else {
            exactSearch(processedQuery, matchedWords);
        }

        return matchedWords;
    }

    private void jokerSearch(String query, List<String> results) {
        if (query.length() == 1) {
            results.addAll(getAllWords());
            return;
        }

        String searchPrefix = query.substring(0, query.length() - 1);
        wordsWithPrefix(root, searchPrefix, "", results);
    }

    private void exactSearch(String query, List<String> results) {
        if (hasWord(query)) {
            results.add(query);
        }
    }

    // recursive
    private void wordsWithPrefix(TrieNode node, String prefix, String builtWord, List<String> results) {
        if (builtWord.length() < prefix.length()) {
            char requiredChar = prefix.charAt(builtWord.length());
            TrieNode childNode = node.children.get(requiredChar);

            if (childNode != null) {
                wordsWithPrefix(childNode, prefix, builtWord + requiredChar, results);
            }
            return;
        }

        if (node.isEndOfWord) {
            results.add(builtWord);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            wordsWithPrefix(entry.getValue(), prefix, builtWord + entry.getKey(), results);
        }
    }

    public int countWords() {
        return counter;
    }

    public List<String> getAllWords() {
        List<String> collectedWords = new ArrayList<>();
        gatherWords(root, "", collectedWords);
        collectedWords.sort(ukrainianCollator); // O (n log n)
        return collectedWords;
    }

    // recursive
    private void gatherWords(TrieNode node, String builtWord, List<String> words) {
        if (node.isEndOfWord) {
            words.add(builtWord);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            gatherWords(entry.getValue(), builtWord + entry.getKey(), words);
        }
    }

   // helpers
    private String checkAndTrim(String word) {
        if (word == null || word.isEmpty()) {
            return null;
        }

        String trimmed = word.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private TrieNode navigateToWordEnd(String word) {
        TrieNode currentNode = root;

        for (char symbol : word.toCharArray()) {
            currentNode.children.putIfAbsent(symbol, new TrieNode());
            currentNode = currentNode.children.get(symbol);
        }

        return currentNode;
    }

    private TrieNode findNodeByPath(String word) {
        TrieNode currentNode = root;

        for (char symbol : word.toCharArray()) {
            TrieNode nextNode = currentNode.children.get(symbol);
            if (nextNode == null) {
                return null;
            }
            currentNode = nextNode;
        }

        return currentNode;
    }

    private void markWord(TrieNode node) {
        node.isEndOfWord = true;
        counter++;
    }

    private void unmarkWord(TrieNode node) {
        node.isEndOfWord = false;
        counter--;
    }

    private boolean hasAsterisk(String query) {
        return query.endsWith("*");
    }
}