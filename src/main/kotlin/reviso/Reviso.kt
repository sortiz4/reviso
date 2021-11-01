package reviso

import java.io.File

class Reviso {
    private var paths: Set<File> = emptySet()
    private var case: Case? = null
    private var search: String? = null
    private var replace: String? = null
    private var isRecursive: Boolean = false
    private var isExpression: Boolean = false
    private var withExtension: Boolean = false

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
        return changes.map { (old, new) -> "${old.padEnd(padding)} -> $new" }
    }

    fun rename(): Int {
        return collectFileChanges().onEach { (source, target) -> source.renameTo(target) }.count()
    }

    fun paths(value: File): Reviso {
        return paths(setOf(value))
    }

    fun paths(value: Set<File>): Reviso {
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

    fun withExtension(value: Boolean): Reviso {
        withExtension = value
        return this
    }

    private fun collectFileChanges(): Collection<Pair<File, File>> {
        val isCase by lazy { case != null }
        val isSearch by lazy { !search.isNullOrEmpty() }

        val search by lazy { search ?: "" }
        val replace by lazy { replace ?: "" }
        val pattern by lazy { search.toPattern() }

        fun transform(source: File): Pair<File, File>? {
            val target = try {
                when {
                    isCase -> when (case) {
                        Case.LOWER -> source.asCloneByLowerCase(withExtension)
                        Case.UPPER -> source.asCloneByUpperCase(withExtension)
                        Case.DOT -> source.asCloneByDotCase(withExtension)
                        Case.KEBAB -> source.asCloneByKebabCase(withExtension)
                        Case.SNAKE -> source.asCloneBySnakeCase(withExtension)
                        Case.CAMEL -> source.asCloneByCamelCase(withExtension)
                        Case.PASCAL -> source.asCloneByPascalCase(withExtension)
                        Case.TITLE -> source.asCloneByTitleCase(withExtension)
                        Case.TITLE_AP -> source.asCloneByTitleApCase(withExtension)
                        Case.SENTENCE -> source.asCloneBySentenceCase(withExtension)
                        else -> null
                    }
                    isSearch -> if (isExpression) {
                        source.asCloneBySearchAndReplace(pattern, replace, withExtension)
                    } else {
                        source.asCloneBySearchAndReplace(search, replace, withExtension)
                    }
                    else -> null
                }
            } catch (_: IllegalArgumentException) {
                null
            }

            target?.let {
                if (source.name != target.name) {
                    // Ignore unchanged file names
                    return Pair(source, target)
                }
            }

            return null
        }

        return collectFiles().mapNotNull { transform(it) }
    }

    private fun collectFiles(): Collection<File> {
        fun transform(file: File): Collection<File> {
            val files = if (!isRecursive) {
                file.walkBottomUp().maxDepth(1)
            } else {
                file.walkBottomUp()
            }
            return files.toList().dropLast(1)
        }

        return paths.map { transform(it.toAbsoluteFile()) }.fold(emptyList()) { sum, item -> sum + item }
    }
}
