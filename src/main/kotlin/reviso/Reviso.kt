package reviso

import java.io.File
import java.nio.file.Path

class Reviso {
    private var paths: Set<Path> = emptySet()
    private var case: Case? = null
    private var search: String? = null
    private var replace: String? = null
    private var isRecursive: Boolean = false
    private var isExpression: Boolean = false

    fun preview(relative: Boolean): Collection<String> {
        fun transform(file: File): File {
            if (!relative) {
                return file
            }
            return file.toRelativeFile()
        }

        // Constructs a formatted preview (pads the first column to enhance readability)
        val changes = collectFileChanges().map { (old, new) -> Pair("${transform(old)}", "${transform(new)}") }
        val padding = changes.maxByOrNull { (old, _) -> old.length }?.first?.length ?: 0
        return changes.map { (old, new) -> "${old.padEnd(padding)} -> $new" }.toList()
    }

    fun rename(): Int {
        return collectFileChanges().onEach { (source, target) -> source.renameTo(target) }.count()
    }

    fun paths(value: Path): Reviso {
        return paths(setOf(value))
    }

    fun paths(value: Set<Path>): Reviso {
        paths = value
        return this
    }

    fun case(value: String): Reviso {
        case = Case.from(value)
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
        val isCase by lazy { case != null }
        val isSearch by lazy { !search.isNullOrEmpty() }

        val search by lazy { search ?: "" }
        val replace by lazy { replace ?: "" }
        val pattern by lazy { search.toPattern() }

        fun transform(source: File): Pair<File, File>? {
            val target = try {
                when {
                    isCase -> when (case) {
                        Case.LOWER -> source.asCloneByLowerCase()
                        Case.UPPER -> source.asCloneByUpperCase()
                        Case.DOT -> source.asCloneByDotCase()
                        Case.KEBAB -> source.asCloneByKebabCase()
                        Case.SNAKE -> source.asCloneBySnakeCase()
                        Case.CAMEL -> source.asCloneByCamelCase()
                        Case.PASCAL -> source.asCloneByPascalCase()
                        Case.TITLE -> source.asCloneByTitleCase()
                        Case.TITLE_AP -> source.asCloneByTitleApCase()
                        Case.SENTENCE -> source.asCloneBySentenceCase()
                        else -> null
                    }
                    isSearch -> when (isExpression) {
                        true -> source.asCloneBySearchAndReplace(pattern, replace)
                        false -> source.asCloneBySearchAndReplace(search, replace)
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
        return paths.map { collectFiles(it.toAbsoluteFile()) }.fold(emptySequence()) { seq, tree -> seq + tree }
    }

    private fun collectFiles(file: File): Sequence<File> {
        return when (isRecursive) {
            true -> file.walkBottomUp()
            false -> file.walkBottomUp().maxDepth(1)
        }
    }
}
