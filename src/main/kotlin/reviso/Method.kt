package reviso

enum class Method(private val long: String, private val short: String) {
    Lower(
        "Lowercase",
        "lowercase",
    ),
    Upper(
        "Uppercase",
        "uppercase",
    ),
    Sentence(
        "Sentence",
        "sentence",
    ),
    TitleAp(
        "Title (AP)",
        "title-ap",
    ),
    TitleSimple(
        "Title (Simple)",
        "title-simple",
    );

    companion object {
        fun choices(): Array<String> {
            return values().map { it.long }.toTypedArray()
        }

        fun from(choice: String): Method? {
            return values().find { it.long == choice || it.short == choice }
        }
    }
}