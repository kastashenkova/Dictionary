# Search Dictionary - Trie-Based Word Storage

A Java implementation of a dictionary using Trie (prefix tree) data structure with support for exact and wildcard searches.

## Features

- **Fast Operations**: O(m) complexity for add, delete, and search (m = word length)
- **Wildcard Search**: Find words by prefix using `*` joker
- **Case-Sensitive**: Distinguishes between uppercase and lowercase
- **Ukrainian Sorting**: Built-in Collator for proper Ukrainian alphabet ordering
- **Automatic Cleanup**: Removes empty branches after word deletion
- **File Import**: Load words from text files

## Core Operations

| Operation | Complexity | Description |
|-----------|------------|-------------|
| Add word | O(m) | Insert word into trie |
| Delete word | O(m) | Remove word and clean empty branches |
| Search word | O(m) | Check if word exists |
| Wildcard search | O(m + k log k) | Find all words with prefix |
| Count words | O(1) | Get total word count |

## Usage

### Running the Application

```bash
javac org/example/*.java
java org.example.Tester <filename> [query1] [query2] ...
```

### Interactive Mode

```bash
java org.example.Tester words.txt
```

### Command-Line Queries

```bash
java org.example.Tester words.txt "кіт*" "собака" "при*"
```

## Input File Format

Words can be separated by spaces, commas, or punctuation:

```
кіт, собака, миша
пташка слон жираф
```

Supports UTF-8 encoding for Ukrainian text.

## Interactive Commands

```
> add <word>          # Add new word to dictionary
> del <word>          # Delete word from dictionary
> has <word>          # Check if word exists
> count               # Show total word count
> <query>             # Search for exact word
> <prefix>*           # Wildcard search (e.g., "кіт*")
> exit                # Exit program
```

## Examples

### Adding Words

```
> add кішка
The new word added into the dictionary

> add кішка
The word already exists
```

### Searching

```
> кіт
______ Query: кіт
Found 1 result(s)
кіт

> кі*
______ Query: кі*
Found 3 result(s)
кіт
кішка
кінь
```

### Deleting Words

```
> del кішка
The word deleted from the dictionary

> del відсутнє
The word not found in the dictionary
```

### Checking Existence

```
> has собака
The word exists

> has кіт123
The word does not exist
```

## Project Structure

```
org/example/
├── SearchDictionary.java  # Trie implementation
└── Tester.java           # CLI interface and file loader
```

## Implementation Details

### Trie Structure

- **Node**: Contains children map and end-of-word flag
- **Root**: Empty node serving as entry point
- **Children**: HashMap for O(1) character lookup

### Key Features

**Word Validation**
- Trims whitespace
- Rejects null or empty strings

**Automatic Cleanup**
- Recursively removes empty branches after deletion
- Maintains optimal memory usage

**Ukrainian Support**
- Uses `Collator` with Ukrainian locale
- Proper sorting: а, б, в, г, ґ, д, е, є, ж, з...

### Wildcard Search Logic

```
"кіт*" → Find all words starting with "кіт"
"*"    → Return all words in dictionary
```

Only supports suffix wildcard (prefix search).

## Time Complexity

- **Add**: O(m) - traverse word length
- **Delete**: O(m) - traverse + cleanup
- **Has**: O(m) - traverse to check
- **Query (exact)**: O(m)
- **Query (wildcard)**: O(m + k log k) - m for prefix, k results sorted
- **Count**: O(1) - counter maintained
- **Get all words**: O(n log n) - n words, sorted

Where:
- m = word length
- k = number of results
- n = total words in dictionary

## Memory Efficiency

- Only stores actual words (not all possible prefixes)
- Shares common prefixes between words
- Removes empty branches on deletion
- Space complexity: O(n × m) worst case

## Example Session

```bash
$ java org.example.Tester ukrainian_words.txt

Dictionary downloaded
We have 1523 words in the dictionary

— Enter some word or query with * for search
— add <word>
— del <word>
— has <word>
— count
— exit

> кіт*
______ Query: кіт*
Found 5 result(s)

кіт
кітик
кітка
кітче
кіття

> add котик
The new word added into the dictionary

> count
Number of words: 1524

> exit
Program completed.
```

## Error Handling

- Invalid words (null/empty) are rejected
- Missing files show error message
- Malformed input is ignored
- Duplicate additions are detected

## Educational Value

Perfect for learning:
- Trie data structure implementation
- Prefix-based searching
- Recursive tree traversal
- File I/O with encoding
- String processing
---

**License**: Educational project - free to use and modify
