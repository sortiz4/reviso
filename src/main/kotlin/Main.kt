import java.io.IOException as IoException
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    @Throws(IoException::class)
    override fun start(stage: Stage) {
        // Load the scene and set the title
        stage.icons.add(Resource.png(Constants.ICON))
        stage.scene = Scene(Resource.fxml(Constants.SCENE))
        stage.title = Constants.TITLE
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
