package reviso.scene

import java.io.File
import javafx.geometry.Insets
import javafx.scene.control.Alert as BaseAlert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.Stage
import reviso.Constants
import reviso.Resource

open class Alert : BaseAlert {
    constructor(type: AlertType) : super(type) {
        val stage = dialogPane.scene.window as Stage
        stage.icons.addAll(Resource.png(Constants.ICON_SET))
        buttonTypes[0] = ButtonType("Ok")
    }
}

open class DirectoryAlert : Alert {
    constructor(message: String) : super(AlertType.ERROR) {
        title = "Directory error"
        headerText = message
        contentText = "Please open a directory to begin."
    }
}

class InvalidDirectoryAlert : DirectoryAlert {
    constructor(path: String) : super("'$path' is not a directory.")
}

class NoDirectoryAlert : DirectoryAlert {
    constructor() : super("No directory was found.")
}

class PreviewAlert : Alert {
    constructor(files: List<Pair<File, File>>) : super(AlertType.INFORMATION) {
        title = "Preview"
        headerText = "Review the changes."
        if(files.isEmpty()) {
            contentText = "No changes detected."
        } else {
            isResizable = true

            // Constructs a formatted preview (pads the first column to enhance readability)
            val reduced = files.map { (old, new) -> Pair(old.name, new.name) }
            val padding = reduced.maxBy { (old, _) -> old.length }?.first?.length!!
            val preview = reduced.map { (old, new) -> "${old.padEnd(padding)} => $new" }

            // View dimensions (rows, columns, insets)
            val cols = preview.maxBy { entry -> entry.length }?.length!!
            val rows = Math.min(preview.size, TextArea.DEFAULT_PREF_ROW_COUNT)
            val inset = 10.0

            // Construct an optimal viewport for the preview
            val text = TextArea(preview.joinToString("\n"))
            text.font = Font.font("Monospaced")
            text.prefColumnCount = cols
            text.prefRowCount = rows
            text.isEditable = false
            text.isWrapText = false

            val box = HBox(text)
            HBox.setHgrow(text, Priority.ALWAYS)
            box.padding = Insets(inset, inset, 0.0, inset)
            dialogPane.content = box
        }
    }
}

class ResultAlert : Alert {
    constructor(count: Int) : super(AlertType.INFORMATION) {
        title = "Result"
        headerText = "Rename complete."
        contentText = if(count > 0) {
            "$count file(s) renamed."
        } else {
            "Nothing was renamed."
        }
    }
}
