package reviso

enum class Method(private val cliName: String, private val guiName: String) {
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
        fun cliNames(): Array<String> {
            return values().map { it.cliName }.toTypedArray()
        }

        fun guiNames(): Array<String> {
            return values().map { it.guiName }.toTypedArray()
        }

        fun from(value: String): Method? {
            return values().find { it.cliName == value || it.guiName == value }
        }
    }
}