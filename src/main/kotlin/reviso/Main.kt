package reviso

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import kotlin.system.exitProcess

class Main : Application() {
    override fun start(stage: Stage) {
        // Load the scene and set the title
        stage.icons.addAll(Resources.icons())
        stage.scene = Scene(Resources.scene())
        stage.title = Resources.title()
        stage.show()

        // Set the window constraints
        stage.minWidth = stage.width
        stage.minHeight = stage.height
        stage.maxHeight = stage.height
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Command { launch(Main::class.java, *args) }.main(args)
            exitProcess(0)
        }
    }
}
