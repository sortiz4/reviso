package reviso

import java.util.Locale

fun String.localeCapitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.toLocaleLowerCase(): String {
    return this.lowercase(Locale.getDefault())
}

fun String.toLocaleUpperCase(): String {
    return this.uppercase(Locale.getDefault())
}
