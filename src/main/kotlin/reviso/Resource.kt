package reviso

import java.io.IOException as IoException
import java.net.URL as Url
import javafx.fxml.FXMLLoader as FxmlLoader
import javafx.scene.Parent
import javafx.scene.image.Image

object Resource {
    @Throws(IoException::class)
    fun fxml(path: String): Parent {
        return FxmlLoader.load(load("$path.fxml"))
    }

    @Throws(IoException::class)
    fun png(path: String): Image {
        return Image(load("$path.png").openStream(), 0.0, 0.0, true, true)
    }

    @Throws(IoException::class)
    fun png(paths: Array<String>): Array<Image> {
        return paths.map { path -> png(path) }.toTypedArray()
    }

    private fun load(path: String): Url {
        return Resource::class.java.classLoader.getResource(path)
    }
}
