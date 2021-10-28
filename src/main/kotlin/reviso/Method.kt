package reviso

enum class Method(private val tag: String, private val choice: String) {
    Lower(
        "lowercase",
        "Lowercase",
    ),
    Upper(
        "uppercase",
        "Uppercase",
    ),
    Sentence(
        "sentence",
        "Sentence",
    ),
    TitleAp(
        "title-ap",
        "Title (AP)",
    ),
    TitleSimple(
        "title-simple",
        "Title (Simple)",
    );

    companion object {
        fun choices(): Array<String> {
            return values().map { it.choice }.toTypedArray()
        }

        fun from(value: String): Method? {
            return values().find { it.choice == value || it.tag == value }
        }
    }
}