package rename

import java.io.File
import java.util.regex.Pattern

object Transform {
    /**
     * Replaces all matches in the file name (regex).
     */
    fun regex(file: File, search: Pattern, replace: String): File {
        val name = search.matcher(file.name).replaceAll(replace)
        return File(file.parent, name)
    }

    /**
     * Replaces all matches in the file name (simple).
     */
    fun simple(file: File, search: String, replace: String): File {
        val name = file.name.replace(search, replace)
        return File(file.parent, name)
    }

    /**
     * Uncapitalizes every letter in the file name.
     */
    fun lower(file: File): File {
        val name = file.name.toLowerCase()
        return File(file.parent, name)
    }

    /**
     * Capitalizes every letter in the file name.
     */
    fun upper(file: File): File {
        val name = file.name.toUpperCase()
        return File(file.parent, name)
    }

    /**
     * Capitalizes the first letter of the file name.
     */
    fun sentence(file: File): File {
        val name = file.name.toLowerCase().capitalize()
        return File(file.parent, name)
    }

    /**
     * Capitalizes the first letter of every word in the file name.
     */
    fun titleSimple(file: File): File {
        val name = file.name.split(' ').joinToString(" ") { s -> s.toLowerCase().capitalize() }
        return File(file.parent, name)
    }
}
