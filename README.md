# Reviso
Reviso is a powerful desktop application capable of renaming large collections
of files at the press of a button. Users can search for simple and complex
patterns using regular expressions, or rely on a commonly used set of methods
to rename their files.

This application was inspired by the long abandoned *Mass File Renamer* hosted
on the now defunct Google Code. It was exclusive to Windows and served me well
for many years.

## Requirements
- Linux, MacOS, or Windows
- Java 12

## Compilation
In addition to the requirements above, those seeking to compile must have...

- Java SDK 16
- Gradle 7.2
- Python 3.8

Once these requirements have been met, simply clone the repository and execute
`gradle build`. A distribution will be created under `./build/distributions/`.

## Usage
Before starting, you must enter the directory containing the files you would
like to rename in the text field under the `Source directory` section. This
can be accomplished in a few different ways...

1. Manually enter the full path to the directory (e.g. `/home/steven`).
2. Selecting a directory from the file explorer by pressing `Browse`.
3. Dragging and dropping a file or directory onto the window.
   - The parent directory will be assumed when a file is given.

Once a path has been entered, you must explicitly press `Open` (or the enter
key if you've entered the path manually). The currently open directory will be
shown below the text field with the label `Open:`.

Once a directory has been opened...
1. Changes can be *reviewed* with the `Preview` button. No files will be renamed.
2. Changes can be *applied* with the `Rename` button. This action cannot be undone.

## Pattern matching
This section allows you to search for and replace patterns. All occurrences of
the search string will be replaced by the replacement string. The replacement
string may be left blank to remove the search string from the file name.

### Options
1. `Regex` will treat the search and replacement strings as regular expressions
   (see the [syntax][1] for more information).
2. `Recursive` will rename all files *under* the currently open directory (i.e.
   all subdirectories will be included).

## Standard methods
This section allows you to rename files based on standard methods.

### Methods
1. `Lowercase` decapitalizes every character.
2. `Uppercase` capitalizes every character.
3. `Sentence` capitalizes the first word.
4. `Title (AP)` capitalizes most words (AP style).
5. `Title (simple)` capitalizes every word.

### Options
1. `Recursive` behaves exactly like the same option above.

[1]: https://docs.oracle.com/javase/9/docs/api/java/util/regex/Pattern.html#sum
