package reviso

import java.net.URL as Url
import javafx.fxml.FXMLLoader as FxmlLoader
import javafx.scene.Parent
import javafx.scene.image.Image
import net.pearx.kasechange.toTitleCase

object Resources {
    fun name(): String {
        return Resources::class.java.`package`.implementationTitle ?: ""
    }

    fun title(): String {
        return name().toTitleCase()
    }

    fun version(): String {
        return Resources::class.java.`package`.implementationVersion ?: ""
    }

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
