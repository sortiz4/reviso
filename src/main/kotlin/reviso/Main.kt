package reviso

import java.io.IOException as IoException
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    @Throws(IoException::class)
    override fun start(stage: Stage) {
        // Load the scene and set the title
        stage.icons.addAll(Resource.png(Constants.ICON_SET))
        stage.scene = Scene(Resource.fxml(Constants.SCENE_ROOT))
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
            launch(Main::class.java, *args)
        }
    }
}
