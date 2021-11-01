package reviso

import java.io.File
import java.util.Locale
import java.util.regex.Pattern
import net.pearx.kasechange.splitToWords
import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toDotCase
import net.pearx.kasechange.toKebabCase
import net.pearx.kasechange.toPascalCase
import net.pearx.kasechange.toSnakeCase
import net.pearx.kasechange.toTitleCase

/**
 * Words that should not be capitalized (associated press).
 */
private val AP_WORDS = hashSetOf(
    "a",
    "an",
    "and",
    "at",
    "but",
    "by",
    "for",
    "in",
    "nor",
    "of",
    "on",
    "or",
    "so",
    "the",
    "to",
    "up",
    "yet",
)

/**
 *
 */
fun String.toLowerCase(): String {
    return lowercase(Locale.getDefault())
}

/**
 *
 */
fun String.toUpperCase(): String {
    return uppercase(Locale.getDefault())
}

/**
 *
 */
fun String.toSentenceCase(): String {
    return toLowerCase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else "$it" }
}

/**
 *
 */
fun File.toAbsoluteFile(): File {
    return File(absolutePath)
}

/**
 *
 */
fun File.toRelativeFile(): File {
    return relativeTo(File(File("").absolutePath))
}

/**
 *
 */
fun File.asCloneByLowerCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toLowerCase() }
}

/**
 *
 */
fun File.asCloneByUpperCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toUpperCase() }
}

/**
 *
 */
fun File.asCloneByDotCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toDotCase() }
}

/**
 *
 */
fun File.asCloneByKebabCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toKebabCase() }
}

/**
 *
 */
fun File.asCloneBySnakeCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toSnakeCase() }
}

/**
 *
 */
fun File.asCloneByCamelCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toCamelCase() }
}

/**
 *
 */
fun File.asCloneByPascalCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toPascalCase() }
}

/**
 * Capitalizes the first letter of every word in the file name (simple).
 */
fun File.asCloneByTitleCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toTitleCase() }
}

/**
 * Capitalizes the first letter of every word in the file name (associated press).
 */
fun File.asCloneByTitleApCase(withExtension: Boolean): File {
    fun transform(name: String): String {
        val words = name.splitToWords()

        fun transform(i: Int, word: String): String {
            if (i == 0 || i == words.size - 1 || word !in AP_WORDS) {
                return word.toTitleCase()
            }
            return word
        }

        return words.mapIndexed { i, word -> transform(i, word.toLowerCase()) }.joinToString(" ")
    }

    return asClone(withExtension) { transform(it) }
}

/**
 * Capitalizes the first letter of the file name.
 */
fun File.asCloneBySentenceCase(withExtension: Boolean): File {
    return asClone(withExtension) { it.toSentenceCase() }
}

/**
 * Replaces all matches in the file name (simple).
 */
fun File.asCloneBySearchAndReplace(search: String, replace: String, withExtension: Boolean): File {
    return asClone(withExtension) { it.replace(search, replace) }
}

/**
 * Replaces all matches in the file name (pattern).
 */
fun File.asCloneBySearchAndReplace(search: Pattern, replace: String, withExtension: Boolean): File {
    return asClone(withExtension) { search.matcher(it).replaceAll(replace) }
}

/**
 * Returns a file if the name is not empty. Throws an exception otherwise.
 */
private fun File.asClone(withExtension: Boolean, callback: (String) -> String): File {
    val dot = if (extension.isNotEmpty()) {
        "."
    } else {
        ""
    }

    val child = if (!isDirectory && !withExtension) {
        "${callback(nameWithoutExtension)}${dot}${extension}"
    } else {
        callback(name)
    }

    if (child.isNotEmpty()) {
        return File(parent, child)
    }

    throw IllegalArgumentException("The new name must not be empty.")
}
