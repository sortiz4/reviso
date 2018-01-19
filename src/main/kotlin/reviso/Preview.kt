package reviso

import java.io.File
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Preview {
    /**
     * Words in AP titles that should be uncapitalized.
     */
    private val AP_WORDS = HashSet(arrayListOf(
            "a", "an", "and", "at", "but", "by", "for", "in", "nor",
            "of", "on", "or", "so", "the", "to", "up", "yet"
    ))

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
     * Capitalizes the first letter of every word in the file name (AP).
     */
    fun titleAp(file: File): File {
        val words = file.name.split(' ') as ArrayList
        for(i in 0 until words.size) {
            words[i] = words[i].toLowerCase()
            if(i == 0 || i == words.size - 1) {
                words[i] = words[i].capitalize()
            } else {
                if(words[i] !in Preview.AP_WORDS) {
                    words[i] = words[i].capitalize()
                }
            }
        }
        val name = words.joinToString(" ")
        return File(file.parent, name)
    }

    /**
     * Capitalizes the first letter of every word in the file name (simple).
     */
    fun titleSimple(file: File): File {
        val name = file.name.split(' ').joinToString(" ") { s -> s.toLowerCase().capitalize() }
        return File(file.parent, name)
    }
}
