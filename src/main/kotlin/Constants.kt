object Constants {
    // Program and dialog text
    val TITLE = "Renamer+"

    // Standard transformations
    val CHOICE_UPPER = "Uppercase"
    val CHOICE_LOWER = "Lowercase"
    val CHOICE_SENTENCE = "Sentence"
    val CHOICE_TITLE_SIMPLE = "Title (simple)"
    val CHOICES: Array<String> = arrayOf(
            CHOICE_UPPER,
            CHOICE_LOWER,
            CHOICE_SENTENCE,
            CHOICE_TITLE_SIMPLE
    )

    fun open(message: String): String {
        return "Open: $message"
    }
}
