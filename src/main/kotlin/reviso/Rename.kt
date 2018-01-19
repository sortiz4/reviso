package reviso

import java.io.File
import java.util.regex.Pattern

object Rename {
    fun regex(file: File, search: Pattern, replace: String) {
        file.renameTo(Preview.regex(file, search, replace))
    }

    fun simple(file: File, search: String, replace: String) {
        file.renameTo(Preview.simple(file, search, replace))
    }

    fun lower(file: File) {
        file.renameTo(Preview.lower(file))
    }

    fun upper(file: File) {
        file.renameTo(Preview.upper(file))
    }

    fun sentence(file: File) {
        file.renameTo(Preview.sentence(file))
    }

    fun titleAp(file: File) {
        file.renameTo(Preview.titleAp(file))
    }

    fun titleSimple(file: File) {
        file.renameTo(Preview.titleSimple(file))
    }
}
