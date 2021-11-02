# Reviso
Reviso is a powerful command-line tool and desktop application that can rename
large collections of files at the press of a button. Users can search for and
replace simple or complex patterns with regular expressions, or choose from a
wide variety of commonly used cases to rename their files.

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
- Rust 1.55

Once these requirements have been met, simply clone the repository and execute
`gradle build`. A distribution will be created under `./build/distributions/`.

## Usage
The `--launch` option will open the desktop application. The `--help` option
will provide more information on how to use the command-line tool. This guide
describes how to use the desktop application but the behavior is similar.

Before starting, you must enter the path to the directory containing the files
you would like to rename in the text field under the `Location` section. This
can be accomplished in a few different ways...

1. Manually enter the full path to the directory.
2. Select the directory from the system by pressing `Browse`.
3. Drag and drop the file or directory from the system onto the window.
   - The parent directory will be entered when a file is given.

Once a path has been entered, you must explicitly press `Open` or the enter
key in the text field. The active directory will be shown below the text field.

Once a directory has been opened...
1. Changes can be previewed with the `Preview` button. Files will not be renamed.
2. Changes can be applied with the `Rename` button. This action cannot be undone.

## Common cases
This section allows you to rename files based on common cases.

### Cases
1. `Lower Case` decapitalizes every character.
2. `Upper Case` capitalizes every character.
3. `Lower Space Case` separates words with a space and decapitalizes every character.
4. `Upper Space Case` separates words with a space and capitalizes every character.
5. `Dot Case` decapitalizes every character and separates words with a dot.
6. `Kebab Case` decapitalizes every character and separates words with a hyphen.
7. `Snake Case` decapitalizes every character and separates words with an underscore.
8. `Camel Case` removes separators and capitalizes each word (excluding the first).
9. `Pascal Case` removes separators and capitalizes each word (including the first).
10. `Title Case` separates and capitalizes words.
11. `Title Case (AP)` separates and capitalizes most words (associated press style).
12. `Sentence Case` decapitalizes every character and capitalizes the first character.

### Options
1. `Include extension` will include the file extension when renaming files.
2. `Recursive` will rename all files under the active directory.

## Search and replace
This section allows you to search for and replace arbitrary patterns. All
occurrences of the search text will be replaced by the replacement text. The
replacement text may be omitted to remove the search text from the name.

### Options
1. `Regular expression` will treat the search and replacement text as
   regular expressions. See the [syntax][1] for more information.
2. `Include extension` is identical to the same option above.
3. `Recursive` is identical to the same option above.

[1]: https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/util/regex/Pattern.html#sum
