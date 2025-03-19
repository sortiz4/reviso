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
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

class Command : Clikt(name = Resources.name()) {
    private val paths: Set<File> by argument().file(canBeFile = false, mustExist = true).multiple().unique()
    private val case: String by option(*CASE_NAMES, help = CASE_HELP).choice(*CASE_CHOICES).default("")
    private val search: String by option(*SEARCH_NAMES, help = SEARCH_HELP).default("")
    private val replace: String by option(*REPLACE_NAMES, help = REPLACE_HELP).default("")
    private val recursive: Boolean by option(*RECURSIVE_NAMES, help = RECURSIVE_HELP).flag(default = false)
    private val expression: Boolean by option(*EXPRESSION_NAMES, help = EXPRESSION_HELP).flag(default = false)
    private val extension: Boolean by option(*EXTENSION_NAMES, help = EXTENSION_HELP).flag(default = false)
    private val launch: Boolean by option(*LAUNCH_NAMES, help = LAUNCH_HELP).flag(default = false)
    private val preview: Boolean by option(*PREVIEW_NAMES, help = PREVIEW_HELP).flag(default = false)

    init {
        versionOption(Resources.version(), names = VERSION_NAMES.toSet())
    }

    override fun run() {
        if (launch) {
            Desktop.main(emptyArray())
        } else {
            val reviso = (
                Reviso()
                    .paths(paths)
                    .case(case)
                    .search(search)
                    .replace(replace)
                    .isRecursive(recursive)
                    .isExpression(expression)
                    .withExtension(extension)
            )

            if (preview) {
                reviso.preview(relative = true).forEach { echo(it) }
            } else {
                reviso.rename()
            }
        }
    }

    companion object {
        // Option names
        private val CASE_NAMES = arrayOf("--case", "-c")
        private val SEARCH_NAMES = arrayOf("--search", "-s")
        private val REPLACE_NAMES = arrayOf("--replace", "-r")
        private val RECURSIVE_NAMES = arrayOf("--recursive", "-R")
        private val EXPRESSION_NAMES = arrayOf("--expression", "-E")
        private val EXTENSION_NAMES = arrayOf("--extension", "-X")
        private val LAUNCH_NAMES = arrayOf("--launch", "-l")
        private val PREVIEW_NAMES = arrayOf("--preview", "-p")
        private val VERSION_NAMES = arrayOf("--version", "-v")

        // Help messages
        private const val CASE_HELP = "The common case to use. Disables search and replace."
        private const val SEARCH_HELP = "Text to search for. Disables case selection."
        private const val REPLACE_HELP = "Text to replace the search text with. Disables case selection."
        private const val RECURSIVE_HELP = "Descend into directories."
        private const val EXPRESSION_HELP = "Interpret the search and replace text as a regular expression."
        private const val EXTENSION_HELP = "Include the file extension."
        private const val LAUNCH_HELP = "Launch the graphical user interface."
        private const val PREVIEW_HELP = "Preview the changes."

        // Extra options
        private val CASE_CHOICES = Case.cliNames()
    }
}
