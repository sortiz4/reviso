package reviso

import java.io.File
import java.nio.file.Path
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
    return toLowerCase().replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else "$c" }
}

/**
 *
 */
fun Path.toAbsoluteFile(): File {
    return File(File("$this").absolutePath)
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
fun File.asCloneByLowerCase(): File {
    return asClone(smartName().toLowerCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneByUpperCase(): File {
    return asClone(smartName().toUpperCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneByDotCase(): File {
    return asClone(smartName().toDotCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneByKebabCase(): File {
    return asClone(smartName().toKebabCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneBySnakeCase(): File {
    return asClone(smartName().toSnakeCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneByCamelCase(): File {
    return asClone(smartName().toCamelCase(), smartExtension())
}

/**
 *
 */
fun File.asCloneByPascalCase(): File {
    return asClone(smartName().toPascalCase(), smartExtension())
}

/**
 * Capitalizes the first letter of every word in the file name (simple).
 */
fun File.asCloneByTitleCase(): File {
    return asClone(smartName().toTitleCase(), smartExtension())
}

/**
 * Capitalizes the first letter of every word in the file name (associated press).
 */
fun File.asCloneByTitleApCase(): File {
    val words = smartName().splitToWords()

    fun transform(i: Int, word: String): String {
        if (i == 0 || i == words.size - 1 || word !in AP_WORDS) {
            return word.toTitleCase()
        }
        return word
    }

    return asClone(words.mapIndexed { i, w -> transform(i, w.toLowerCase()) }.joinToString(" "), smartExtension())
}

/**
 * Capitalizes the first letter of the file name.
 */
fun File.asCloneBySentenceCase(): File {
    return asClone(smartName().toSentenceCase(), smartExtension())
}

/**
 * Replaces all matches in the file name (simple).
 */
fun File.asCloneBySearchAndReplace(search: String, replace: String): File {
    return asClone(name.replace(search, replace))
}

/**
 * Replaces all matches in the file name (pattern).
 */
fun File.asCloneBySearchAndReplace(search: Pattern, replace: String): File {
    return asClone(search.matcher(name).replaceAll(replace))
}

/**
 * Returns a file if the name is not empty. Throws an exception otherwise.
 */
private fun File.asClone(child: String): File {
    if (child.isNotEmpty()) {
        return File(parent, child)
    }
    throw IllegalArgumentException("The child must not be empty.")
}

/**
 * Returns a file if the name is not empty. Throws an exception otherwise.
 */
private fun File.asClone(name: String, extension: String): File {
    val dot = if (extension.isNotEmpty()) {
        "."
    } else {
        ""
    }
    return asClone("$name$dot$extension")
}

/**
 *
 */
private fun File.smartName(): String {
    return if (!isDirectory) {
        nameWithoutExtension
    } else {
        name
    }
}

/**
 *
 */
private fun File.smartExtension(): String {
    return if (!isDirectory) {
        extension
    } else {
        ""
    }
}
