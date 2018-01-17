package rename.scene

import java.io.File
import javafx.collections.FXCollections as FxCollections
import javafx.geometry.Insets
import javafx.scene.control.Alert as BaseAlert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font

open class Alert : BaseAlert {
    constructor(type: AlertType) : super(type) {
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

class Preview : Alert {
    constructor(pairs: ArrayList<Pair<File, File>>) : super(AlertType.INFORMATION) {
        this.title = "Preview"
        this.headerText = "Review the changes."
        this.isResizable = true

        // Constructs a formatted preview (pads the first column to enhance readability)
        val paths = pairs.map { pair -> Pair("'${pair.first.name}'", "'${pair.second.name}'") }
        val max = paths.maxBy { pair -> pair.first.length }?.first?.length!!
        val table = paths.map { pair -> "${pair.first.padEnd(max)} => ${pair.second}" }
        val cols = table.maxBy { entry -> entry.length }?.length!!
        val inset = 10.0

        // Construct an optimal viewport for the preview
        val text = TextArea(table.joinToString("\n"))
        text.font = Font.font("Monospaced")
        text.prefColumnCount = cols
        text.isEditable = false
        text.isWrapText = false
        val box = HBox(text)
        HBox.setHgrow(text, Priority.ALWAYS)
        box.padding = Insets(inset, inset, 0.0, inset)
        this.dialogPane.content = box
    }
}
