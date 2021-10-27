package reviso

import java.io.File
import java.nio.file.Path
import java.util.regex.Pattern

class Reviso {
    private var paths: Set<Path> = setOf()
    private var mode: String = ""
    private var search: String = ""
    private var replace: String = ""
    private var regex: Boolean = false
    private var preview: Boolean = false
    private var recursive: Boolean = false

    fun preview(): Reviso {
        if (mode.isNotEmpty()) {
            // Do mode...
        } else {
            // Do search and replace...
        }
        return this
    }

    fun rename(): Reviso {
        if (mode.isNotEmpty()) {
            // Do mode...
        } else {
            // Do search and replace...
        }
        return this
    }

    fun setPaths(value: Set<Path>): Reviso {
        paths = value
        return this
    }

    fun setMode(value: String): Reviso {
        mode = value
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

    fun setPreview(value: Boolean): Reviso {
        preview = value
        return this
    }

    fun setRecursive(value: Boolean): Reviso {
        recursive = value
        return this
    }

    private fun collect(): List<Pair<File, File>> {
        val pairs = ArrayList<Pair<File, File>>()
        val pattern = when(regex) {
            true -> Pattern.compile(search)
            false -> null
        }

        // Collect a preview of each renamed file
        for (path in paths) {
            for (source in fileTree(path)) {
                val target = try {
                    when (regex) {
                        true -> Preview.regex(source, pattern!!, replace)
                        false -> Preview.simple(source, search, replace)
                    }
                } catch (_: IllegalArgumentException) {
                    continue
                }
                if (source.name != target.name) {
                    // Ignore unchanged file names
                    pairs.add(Pair(source, target))
                }
            }
        }
        return pairs
    }

    private fun fileTree(path: Path): Sequence<File> {
        return fileTree(path.toString())
    }

    private fun fileTree(path: String): Sequence<File> {
        return fileTree(File(path))
    }

    private fun fileTree(path: File): Sequence<File> {
        // Collect the file tree as a sequence
        return when(recursive) {
            true -> path.walkBottomUp()
            false -> path.walkBottomUp().maxDepth(1)
        }
    }
}
