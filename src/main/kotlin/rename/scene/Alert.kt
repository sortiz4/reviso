package rename.scene

import java.io.File
import javafx.geometry.Insets
import javafx.scene.control.Alert as BaseAlert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.Stage

open class Alert : BaseAlert {
    constructor(type: AlertType) : super(type) {
        val stage = this.dialogPane.scene.window as Stage
        stage.icons.add(Resource.png(Constants.ICON))
        this.buttonTypes[0] = ButtonType("Ok")
    }
}

open class PathAlert : Alert {
    constructor(message: String) : super(AlertType.ERROR) {
        this.title = "Path error"
        this.headerText = message
        this.contentText = "Please open a directory to begin."
    }
}

class EmptyPathAlert : PathAlert {
    constructor() : super("No directory was found.")
}

class InvalidPathAlert : PathAlert {
    constructor(path: String) : super("'$path' is not a directory.")
}

class PreviewAlert : Alert {
    constructor(files: ArrayList<Pair<File, File>>) : super(AlertType.INFORMATION) {
        this.title = "Preview"
        this.headerText = "Review the changes."
        this.isResizable = true

        // Constructs a formatted preview (pads the first column to enhance readability)
        val paths = files.map { pair -> Pair("'${pair.first.name}'", "'${pair.second.name}'") }
        val size = paths.maxBy { pair -> pair.first.length }?.first?.length!!
        val list = paths.map { pair -> "${pair.first.padEnd(size)} => ${pair.second}" }
        val cols = list.maxBy { entry -> entry.length }?.length!!
        val rows = Math.min(list.size, TextArea.DEFAULT_PREF_ROW_COUNT)
        val inset = 10.0

        // Construct an optimal viewport for the preview
        val text = TextArea(list.joinToString("\n"))
        text.font = Font.font("Monospaced")
        text.prefColumnCount = cols
        text.prefRowCount = rows
        text.isEditable = false
        text.isWrapText = false

        val box = HBox(text)
        HBox.setHgrow(text, Priority.ALWAYS)
        box.padding = Insets(inset, inset, 0.0, inset)
        this.dialogPane.content = box
    }
}
