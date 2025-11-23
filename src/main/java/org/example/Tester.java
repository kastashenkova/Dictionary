package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

 /*Формат файлу зі словами:
 — одне або багато слів на рядок;
 — слова можуть бути розділені пробілами, комами, крапками тощо;
 — регістр важливий (слова "Слово" і "слово" вважаються різними)*/

public class Tester {

    private final SearchDictionary dictionary;
    private final Scanner scanner;

    public Tester() {
        dictionary = new SearchDictionary();
        scanner = new Scanner(System.in);
    }

    public void takeFromFile(String filename) {
        Set<String> wordSet = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("[\\s,.;:!?()\\[\\]{}\"']+");

                for (String word : words) {
                    word = word.trim();
                    if (!word.isEmpty()) {
                        wordSet.add(word);
                    }
                }
            }

            for (String word : wordSet) dictionary.addWord(word, true);

            System.out.println("\nDictionary downloaded");
            System.out.println("We have " + dictionary.countWords() + " words in the dictionary");

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        dictionary.countWords();
    }

    public void query(String query) {
        System.out.println("\n______ Query: " + query);

        Iterable<String> results = dictionary.query(query);

        List<String> resultList = new ArrayList<>();
        for (String word : results) {
            resultList.add(word);
        }

        if (resultList.isEmpty()) {
            System.out.println("No results found");
        } else {
            System.out.println("Found " + resultList.size() + " result(s)");
            System.out.println();

            for (String word : resultList) System.out.println(word);
        }
    }

    public void menu() {
        System.out.println("\n— Enter some word or query with * for search");
        System.out.println("— add <word>");
        System.out.println("— del <word>");
        System.out.println("— has <word>");
        System.out.println("— count");
        System.out.println("— exit");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Program completed.");
                break;
            }

            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();

            switch (command) {
                case "add":
                    if (parts.length > 1) {
                        dictionary.addWord(parts[1]);
                    } else {
                        System.out.println("Usage: add <word>");
                    }
                    break;

                case "del":
                    if (parts.length > 1) {
                        String deleted = dictionary.delWord(parts[1]);

                        if (deleted != null) {
                            System.out.println("The word deleted from the dictionary");
                        } else {
                            System.out.println("The word not found in the dictionary");
                        }
                    } else {
                        System.out.println("Usage: del <word>");
                    }
                    break;

                case "has":
                    if (parts.length > 1) {
                        boolean exists = dictionary.hasWord(parts[1]);
                        System.out.println(exists ? "The word exists" : "The word does not exist");
                    } else {
                        System.out.println("Usage: has <word>");
                    }
                    break;

                case "count":
                    System.out.println("Number of words: " + dictionary.countWords());
                    break;

                default:
                    query(input);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Tester tester = new Tester();

        if (args.length == 0) {
            System.err.println("Usage: java org.example.Tester <filename> [query1] [query2] ...");
            return;
        }

        String filename = args[0];
        tester.takeFromFile(filename);

        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                tester.query(args[i]);
            }
        } else {
            tester.menu();
        }
    }
}