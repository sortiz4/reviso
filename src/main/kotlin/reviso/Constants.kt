package reviso

import java.net.URL as Url
import javafx.fxml.FXMLLoader as FxmlLoader
import javafx.scene.Parent
import javafx.scene.image.Image

object Constants {
    // Text and dialog
    const val TITLE = "Reviso"
    const val VERSION = "2.0"

    // Standard transformations
    const val CHOICE_LOWER = "Lowercase"
    const val CHOICE_UPPER = "Uppercase"
    const val CHOICE_SENTENCE = "Sentence"
    const val CHOICE_TITLE_AP = "Title (AP)"
    const val CHOICE_TITLE_SIMPLE = "Title (simple)"
    val CHOICES = arrayOf(
        CHOICE_LOWER,
        CHOICE_UPPER,
        CHOICE_SENTENCE,
        CHOICE_TITLE_AP,
        CHOICE_TITLE_SIMPLE,
    )

    fun icons(): Array<Image> {
        val icons = arrayOf(
            "reviso/icon/16.png",
            "reviso/icon/32.png",
            "reviso/icon/64.png",
            "reviso/icon/128.png",
            "reviso/icon/256.png",
        )
        return icons.map { Image(resource(it)?.openStream(), 0.0, 0.0, true, true) }.toTypedArray()
    }

    fun name(): String {
        return TITLE.toLocaleLowerCase()
    }

    fun open(message: String): String {
        return "Open: $message"
    }

    fun scene(): Parent {
        return FxmlLoader.load(resource("reviso/scene/Root.fxml"))
    }

    private fun resource(path: String): Url? {
        return Constants::class.java.classLoader.getResource(path)
    }
}
