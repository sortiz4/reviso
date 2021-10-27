package reviso

import com.github.ajalt.clikt.core.CliktCommand as Clikt
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.unique
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class Cli(private val start: () -> Unit) : Clikt(name = Constants.name()) {
    private val paths: Set<Path> by argument().path(canBeFile = false, mustExist = true).multiple().unique()
    private val method: String by option(*METHOD_NAMES, help = METHOD_HELP).default("")
    private val search: String by option(*SEARCH_NAMES, help = SEARCH_HELP).default("")
    private val replace: String by option(*REPLACE_NAMES, help = REPLACE_HELP).default("")
    private val regex: Boolean by option(*REGEX_NAMES, help = REGEX_HELP).flag(default = false)
    private val preview: Boolean by option(*PREVIEW_NAMES, help = PREVIEW_HELP).flag(default = false)
    private val recursive: Boolean by option(*RECURSIVE_NAMES, help = RECURSIVE_HELP).flag(default = false)
    private val launch: Boolean by option(*LAUNCH_NAMES, help = LAUNCH_HELP).flag(default = false)

    init {
        versionOption(Constants.VERSION, names = VERSION_NAMES)
    }

    override fun run() {
        when (launch) {
            true -> {
                start()
            }
            false -> {
                val reviso = (
                    Reviso()
                        .setPaths(paths)
                        .setMethod(method)
                        .setSearch(search)
                        .setReplace(replace)
                        .setRegex(regex)
                        .setRecursive(recursive)
                )
                if (preview) {
                    println(reviso.preview().joinToString("\n"))
                }
            }
        }
    }

    companion object {
        // Option names
        private val METHOD_NAMES = arrayOf("--method", "-m")
        private val SEARCH_NAMES = arrayOf("--search", "-s")
        private val REPLACE_NAMES = arrayOf("--replace", "-r")
        private val REGEX_NAMES = arrayOf("--regex", "-E")
        private val PREVIEW_NAMES = arrayOf("--preview", "-p")
        private val RECURSIVE_NAMES = arrayOf("--recursive", "-R")
        private val LAUNCH_NAMES = arrayOf("--launch", "-l")
        private val VERSION_NAMES = setOf("--version", "-v")

        // Help messages
        private const val METHOD_HELP = "The standard method to use. Disables search and replace."
        private const val SEARCH_HELP = "Text to search for. Disables method selection."
        private const val REPLACE_HELP = "Text to replace the search text with. Disables method selection."
        private const val REGEX_HELP = "Interpret the search and replace text as a regular expression."
        private const val PREVIEW_HELP = "Preview the changes."
        private const val RECURSIVE_HELP = "Descend into directories."
        private const val LAUNCH_HELP = "Launch the user interface."
    }
}
