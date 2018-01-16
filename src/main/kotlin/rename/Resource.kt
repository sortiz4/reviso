package rename

import java.io.IOException as IoException
import java.net.URL as Url
import javafx.fxml.FXMLLoader as FxmlLoader
import javafx.scene.Parent

object Resource {
    @Throws(IoException::class)
    fun fxml(path: String): Parent {
        return FxmlLoader.load(Resource.load(path + ".fxml"))
    }

    private fun load(path: String): Url {
        return Resource::class.java.classLoader.getResource(path)
    }
}
