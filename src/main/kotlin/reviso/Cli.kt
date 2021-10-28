package reviso

import com.github.ajalt.clikt.core.CliktCommand as Clikt
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.unique
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

class Cli(private val gui: () -> Unit) : Clikt(name = Constants.name()) {
    private val paths: Set<Path> by argument().path(canBeFile = false, mustExist = true).multiple().unique()
    private val method: String by option(*METHOD_NAMES, help = METHOD_HELP).choice(*METHOD_CHOICES).default("")
    private val search: String by option(*SEARCH_NAMES, help = SEARCH_HELP).default("")
    private val replace: String by option(*REPLACE_NAMES, help = REPLACE_HELP).default("")
    private val recursive: Boolean by option(*RECURSIVE_NAMES, help = RECURSIVE_HELP).flag(default = false)
    private val expression: Boolean by option(*EXPRESSION_NAMES, help = EXPRESSION_HELP).flag(default = false)
    private val launch: Boolean by option(*LAUNCH_NAMES, help = LAUNCH_HELP).flag(default = false)
    private val preview: Boolean by option(*PREVIEW_NAMES, help = PREVIEW_HELP).flag(default = false)

    init {
        versionOption(Constants.VERSION, names = VERSION_NAMES.toSet())
    }

    override fun run() {
        when (launch) {
            true -> {
                gui()
            }
            false -> {
                val reviso = (
                    Reviso()
                        .paths(paths)
                        .method(method)
                        .search(search)
                        .replace(replace)
                        .isRecursive(recursive)
                        .isExpression(expression)
                )
                if (preview) {
                    reviso.preview(relative = true).forEach { echo(it) }
                }
            }
        }
    }

    companion object {
        // Option names
        private val METHOD_NAMES = arrayOf("--method", "-m")
        private val SEARCH_NAMES = arrayOf("--search", "-s")
        private val REPLACE_NAMES = arrayOf("--replace", "-r")
        private val RECURSIVE_NAMES = arrayOf("--recursive", "-R")
        private val EXPRESSION_NAMES = arrayOf("--expression", "-E")
        private val LAUNCH_NAMES = arrayOf("--launch", "-l")
        private val PREVIEW_NAMES = arrayOf("--preview", "-p")
        private val VERSION_NAMES = arrayOf("--version", "-v")

        // Help messages
        private const val METHOD_HELP = "The standard method to use. Disables search and replace."
        private const val SEARCH_HELP = "Text to search for. Disables method selection."
        private const val REPLACE_HELP = "Text to replace the search text with. Disables method selection."
        private const val RECURSIVE_HELP = "Descend into directories."
        private const val EXPRESSION_HELP = "Interpret the search and replace text as a regular expression."
        private const val LAUNCH_HELP = "Launch the graphical user interface."
        private const val PREVIEW_HELP = "Preview the changes."

        // Extra details
        private val METHOD_CHOICES = Method.cliNames()
    }
}
