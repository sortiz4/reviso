package reviso

import java.net.URL as Url
import javafx.fxml.FXMLLoader as FxmlLoader
import javafx.scene.Parent
import javafx.scene.image.Image

object Resources {
    const val TITLE = "Reviso"
    const val VERSION = "2.0.0"

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

    fun scene(): Parent {
        return FxmlLoader.load(resource("reviso/scene/Main.fxml"))
    }

    private fun resource(path: String): Url? {
        return Resources::class.java.classLoader.getResource(path)
    }
}
