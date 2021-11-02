package reviso

enum class Case(private val cliName: String, private val guiName: String) {
    LOWER(
        "lower",
        "Lower Case",
    ),
    UPPER(
        "upper",
        "Upper Case",
    ),
    LOWER_SPACE(
        "lower-space",
        "Lower Space Case",
    ),
    UPPER_SPACE(
        "upper-space",
        "Upper Space Case",
    ),
    DOT(
        "dot",
        "Dot Case",
    ),
    KEBAB(
        "kebab",
        "Kebab Case",
    ),
    SNAKE(
        "snake",
        "Snake Case",
    ),
    CAMEL(
        "camel",
        "Camel Case",
    ),
    PASCAL(
        "pascal",
        "Pascal Case",
    ),
    TITLE(
        "title",
        "Title Case",
    ),
    TITLE_AP(
        "title-ap",
        "Title Case (AP)",
    ),
    SENTENCE(
        "sentence",
        "Sentence Case",
    );

    companion object {
        fun cliNames(): Array<String> {
            return values().map { it.cliName }.toTypedArray()
        }

        fun guiNames(): Array<String> {
            return values().map { it.guiName }.toTypedArray()
        }

        fun from(value: String): Case? {
            return values().find { it.cliName == value || it.guiName == value }
        }
    }
}