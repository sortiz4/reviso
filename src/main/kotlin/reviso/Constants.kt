package reviso

object Constants {
    // Text and dialog
    const val TITLE = "Reviso"

    // Assets and resources
    private const val ICON_16 = "reviso/icon/16"
    private const val ICON_32 = "reviso/icon/32"
    private const val ICON_64 = "reviso/icon/64"
    private const val ICON_128 = "reviso/icon/128"
    private const val ICON_256 = "reviso/icon/256"
    val ICON_SET = arrayOf(
        ICON_16,
        ICON_32,
        ICON_64,
        ICON_128,
        ICON_256
    )
    const val SCENE_ROOT = "reviso/scene/Root"

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
