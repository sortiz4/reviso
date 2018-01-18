package reviso

import java.io.File
import java.util.regex.Pattern

object Rename {
    fun regex(file: File, search: Pattern, replace: String) {
        file.renameTo(Transform.regex(file, search, replace))
    }

    fun simple(file: File, search: String, replace: String) {
        file.renameTo(Transform.simple(file, search, replace))
    }

    fun lower(file: File) {
        file.renameTo(Transform.lower(file))
    }

    fun upper(file: File) {
        file.renameTo(Transform.upper(file))
    }

    fun sentence(file: File) {
        file.renameTo(Transform.sentence(file))
    }

    fun titleAp(file: File) {
        file.renameTo(Transform.titleAp(file))
    }

    fun titleSimple(file: File) {
        file.renameTo(Transform.titleSimple(file))
    }
}
