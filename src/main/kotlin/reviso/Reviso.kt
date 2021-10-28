package reviso

import java.io.File
import java.nio.file.Path
import java.util.regex.Pattern

class Reviso {
    private var paths: Set<Path> = emptySet()
    private var method: Method? = null
    private var search: String? = null
    private var replace: String? = null
    private var isRecursive: Boolean = false
    private var isExpression: Boolean = false

    fun preview(relative: Boolean): Collection<String> {
        fun transform(file: File): File {
            if (!relative) {
                return file
            }
            return file.relativeTo(File(File("").absolutePath))
        }

        // Constructs a formatted preview (pads the first column to enhance readability)
        val changes = collectFileChanges().map { (old, new) -> Pair("${transform(old)}", "${transform(new)}") }
        val padding = changes.maxByOrNull { (old, _) -> old.length }?.first?.length ?: 0
        return changes.map { (old, new) -> "${old.padEnd(padding)} -> $new" }.toList()
    }

    fun rename(): Int {
        return collectFileChanges().onEach { (source, target) -> source.renameTo(target) }.count()
    }

    fun paths(value: Set<Path>): Reviso {
        paths = value
        return this
    }

    fun method(value: String): Reviso {
        method = Method.from(value)
        return this
    }

    fun search(value: String): Reviso {
        search = value
        return this
    }

    fun replace(value: String): Reviso {
        replace = value
        return this
    }

    fun isRecursive(value: Boolean): Reviso {
        isRecursive = value
        return this
    }

    fun isExpression(value: Boolean): Reviso {
        isExpression = value
        return this
    }

    private fun collectFileChanges(): Sequence<Pair<File, File>> {
        val isSearch by lazy { !search.isNullOrEmpty() }
        val isStandard by lazy { method != null }

        val search by lazy { search ?: "" }
        val replace by lazy { replace ?: "" }
        val pattern by lazy { Pattern.compile(search) }

        fun transform(source: File): Pair<File, File>? {
            val target = try {
                when {
                    isSearch -> when (isExpression) {
                        true -> renameWithPattern(source, pattern, replace)
                        false -> renameWithSimple(source, search, replace)
                    }
                    isStandard -> when (method) {
                        Method.Lower -> renameToLower(source)
                        Method.Upper -> renameToUpper(source)
                        Method.Sentence -> renameToSentence(source)
                        Method.TitleAp -> renameToTitleAp(source)
                        Method.TitleSimple -> renameToTitleSimple(source)
                        else -> null
                    }
                    else -> null
                }
            } catch (_: IllegalArgumentException) {
                null
            }

            return target?.let {
                if (source.name != target.name) {
                    // Ignore unchanged file names
                    return Pair(source, target)
                }
                return null
            }
        }

        return collectFiles().mapNotNull { transform(it) }
    }

    private fun collectFiles(): Sequence<File> {
        return paths.map { collectFiles(File("$it").absolutePath) }.fold(emptySequence()) { seq, tree -> seq + tree }
    }

    private fun collectFiles(path: File): Sequence<File> {
        return when (isRecursive) {
            true -> path.walkBottomUp()
            false -> path.walkBottomUp().maxDepth(1)
        }
    }

    private fun collectFiles(path: String): Sequence<File> {
        return collectFiles(File(path))
    }

    companion object {
        /**
         * Words in AP titles that should not be capitalized.
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
         * Decapitalizes every letter in the file name.
         */
        private fun renameToLower(file: File): File {
            return createFile(file.parent, file.name.toLocaleLowerCase())
        }

        /**
         * Capitalizes every letter in the file name.
         */
        private fun renameToUpper(file: File): File {
            return createFile(file.parent, file.name.toLocaleUpperCase())
        }

        /**
         * Capitalizes the first letter of the file name.
         */
        private fun renameToSentence(file: File): File {
            return createFile(file.parent, file.name.toLocaleLowerCase().localeCapitalize())
        }

        /**
         * Capitalizes the first letter of every word in the file name (AP).
         */
        private fun renameToTitleAp(file: File): File {
            val words = file.name.split(" ") as MutableList
            for (i in 0 until words.size) {
                words[i] = words[i].toLocaleLowerCase()
                if (i == 0 || i == words.size - 1 || words[i] !in AP_WORDS) {
                    words[i] = words[i].localeCapitalize()
                }
            }
            return createFile(file.parent, words.joinToString(" "))
        }

        /**
         * Capitalizes the first letter of every word in the file name (simple).
         */
        private fun renameToTitleSimple(file: File): File {
            fun transform(word: String): String {
                return word.toLocaleLowerCase().localeCapitalize()
            }

            return createFile(file.parent, file.name.split(" ").joinToString(" ") { transform(it) })
        }

        /**
         * Replaces all matches in the file name (pattern).
         */
        private fun renameWithPattern(file: File, search: Pattern, replace: String): File {
            return createFile(file.parent, search.matcher(file.name).replaceAll(replace))
        }

        /**
         * Replaces all matches in the file name (simple).
         */
        private fun renameWithSimple(file: File, search: String, replace: String): File {
            return createFile(file.parent, file.name.replace(search, replace))
        }

        /**
         * Returns a file if the name is not empty. Throws an exception otherwise.
         */
        private fun createFile(parent: String, child: String): File {
            if (child.isNotEmpty()) {
                return File(parent, child)
            }
            throw IllegalArgumentException("The replacement must not be empty.")
        }
    }
}
