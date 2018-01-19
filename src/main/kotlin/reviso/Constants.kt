package reviso

object Constants {
    // Text and dialog
    const val TITLE = "Reviso"

    // Assets and resources
    const val ICON = "reviso/scene/icon"
    const val ROOT = "reviso/scene/Root"

    // Standard transformations
    const val CHOICE_UPPER = "Uppercase"
    const val CHOICE_LOWER = "Lowercase"
    const val CHOICE_SENTENCE = "Sentence"
    const val CHOICE_TITLE_AP = "Title (AP)"
    const val CHOICE_TITLE_SIMPLE = "Title (simple)"
    val CHOICES = arrayOf(
            CHOICE_LOWER,
            CHOICE_UPPER,
            CHOICE_SENTENCE,
            CHOICE_TITLE_AP,
            CHOICE_TITLE_SIMPLE
    )

    fun open(message: String): String {
        return "Open: $message"
    }
}
