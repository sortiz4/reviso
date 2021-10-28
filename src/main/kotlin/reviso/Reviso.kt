package reviso

import java.io.File
import java.nio.file.Path
import java.util.regex.Pattern

class Reviso {
    private var paths: Set<Path> = emptySet()
    private var method: Method? = null
    private var search: String? = null
    private var replace: String? = null
    private var regex: Boolean = false
    private var recursive: Boolean = false

    fun preview(): List<String> {
        // Constructs a formatted preview (pads the first column to enhance readability)
        val pairs = collectFilePairs().map { (old, new) -> Pair("${relative(old)}", "${relative(new)}") }
        val padding = pairs.maxByOrNull { (old, _) -> old.length }?.first?.length ?: 0
        return pairs.map { (old, new) -> "${old.padEnd(padding)} -> $new" }
    }

    fun rename(): Int {
        val pairs = collectFilePairs()
        for ((source, target) in pairs) {
            source.renameTo(target)
        }
        return pairs.size
    }

    fun setPaths(value: Set<Path>): Reviso {
        paths = value
        return this
    }

    fun setMethod(value: String): Reviso {
        method = Method.from(value)
        return this
    }

    fun setSearch(value: String): Reviso {
        search = value
        return this
    }

    fun setReplace(value: String): Reviso {
        replace = value
        return this
    }

    fun setRegex(value: Boolean): Reviso {
        regex = value
        return this
    }

    fun setRecursive(value: Boolean): Reviso {
        recursive = value
        return this
    }

    private fun collectFilePairs(): Collection<Pair<File, File>> {
        val pattern by lazy { Pattern.compile(search ?: "") }
        val filePairs = mutableListOf<Pair<File, File>>()

        // Collect a preview of each renamed file
        for (source in collectFiles()) {
            val target = try {
                if (isSearch()) {
                    when (regex) {
                        true -> renameWithPattern(source, pattern, replace ?: "")
                        false -> renameWithString(source, search ?: "", replace ?: "")
                    }
                } else if (isStandard()) {
                    when (method) {
                        Method.Lower -> renameToLower(source)
                        Method.Upper -> renameToUpper(source)
                        Method.Sentence -> renameToSentence(source)
                        Method.TitleAp -> renameToTitleAp(source)
                        Method.TitleSimple -> renameToTitleSimple(source)
                        else -> continue
                    }
                } else {
                    continue
                }
            } catch (_: IllegalArgumentException) {
                continue
            }
            if (source.name != target.name) {
                // Ignore unchanged file names
                filePairs.add(Pair(source, target))
            }
        }
        return filePairs
    }

    private fun collectFiles(): Sequence<File> {
        return paths.map { collectFiles(File("$it").absolutePath) }.fold(emptySequence()) { seq, tree -> seq + tree }
    }

    private fun collectFiles(path: File): Sequence<File> {
        return when(recursive) {
            true -> path.walkBottomUp()
            false -> path.walkBottomUp().maxDepth(1)
        }
    }

    private fun collectFiles(path: String): Sequence<File> {
        return collectFiles(File(path))
    }

    private fun isSearch(): Boolean {
        return search.isNullOrEmpty()
    }

    private fun isStandard(): Boolean {
        return method != null
    }

    private fun relative(file: File): File {
        return file.relativeTo(File(File("").absolutePath))
    }

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
     * Replaces all matches in the file name (regex).
     */
    private fun renameWithPattern(file: File, search: Pattern, replace: String): File {
        return createFile(file.parent, search.matcher(file.name).replaceAll(replace))
    }

    /**
     * Replaces all matches in the file name (simple).
     */
    private fun renameWithString(file: File, search: String, replace: String): File {
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
    }
}
