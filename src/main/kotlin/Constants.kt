object Constants {
    // Text and dialog
    val TITLE = "Rename"

    // Assets and resources
    val ICON = "rename/scene/icon"
    val ROOT = "rename/scene/Root"

    // Standard transformations
    val CHOICE_UPPER = "Uppercase"
    val CHOICE_LOWER = "Lowercase"
    val CHOICE_SENTENCE = "Sentence"
    val CHOICE_TITLE_AP = "Title (AP)"
    val CHOICE_TITLE_SIMPLE = "Title (simple)"
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
