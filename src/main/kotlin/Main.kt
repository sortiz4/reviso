import java.io.IOException as IoException
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import rename.Resource

class Main : Application() {
    @Throws(IoException::class)
    override fun start(stage: Stage) {
        // Load the scene and set some basic properties
        val root = Resource.fxml("rename/scene/Main")
        stage.scene = Scene(root)
        stage.title = "Renamer"
        stage.show()

        // Set the window constraints
        stage.minWidth = stage.width
        stage.minHeight = stage.height
        stage.maxHeight = stage.height
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(Main::class.java, *args)
        }
    }
}
