package rename

import java.io.File

object Standard {
    fun lower(file: File): File {
        val name = file.name.toLowerCase()
        return File(file.parent, name)
    }

    fun upper(file: File): File {
        val name = file.name.toUpperCase()
        return File(file.parent, name)
    }
}
