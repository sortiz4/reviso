package rename

import java.io.File

object Rename {
    fun lower(file: File) {
        file.renameTo(Standard.lower(file))
    }

    fun upper(file: File) {
        file.renameTo(Standard.upper(file))
    }
}
