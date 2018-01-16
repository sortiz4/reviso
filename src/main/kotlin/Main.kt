import java.io.IOException as IoException
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import rename.Resource

class Main : Application() {
    @Throws(IoException::class)
    override fun start(primaryStage: Stage) {
        // Load the scene and set some basic properties
        val root = Resource.fxml("rename/Main")
        primaryStage.scene = Scene(root)
        primaryStage.title = "Renamer"
        primaryStage.show()

        // Set the window constraints
        primaryStage.minWidth = primaryStage.width
        primaryStage.minHeight = primaryStage.height
        primaryStage.maxHeight = primaryStage.height
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(Main::class.java, *args)
        }
    }
}
