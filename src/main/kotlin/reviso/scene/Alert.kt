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

open class Alert(type: AlertType) : BaseAlert(type) {
    init {
        val stage = dialogPane.scene.window as Stage
        stage.icons.addAll(Resource.png(Constants.ICON_SET))
        buttonTypes[0] = ButtonType("Ok")
    }
}

open class DirectoryAlert(message: String) : Alert(AlertType.ERROR) {
    init {
        title = "Error"
        headerText = message
        contentText = "Please open a directory to begin."
    }
}

class InvalidDirectoryAlert(path: String) : DirectoryAlert("'$path' is not a directory.")

class NoDirectoryAlert : DirectoryAlert("No directory was found.")

class PreviewAlert(files: List<Pair<File, File>>) : Alert(AlertType.CONFIRMATION) {
    init {
        title = "Preview"
        headerText = "Preview the changes."
        buttonTypes.remove(1, buttonTypes.size)
        if (files.isEmpty()) {
            contentText = "No changes detected."
        } else {
            isResizable = true

            // Constructs a formatted preview (pads the first column to enhance readability)
            val reduced = files.map { (old, new) -> Pair(old.name, new.name) }
            val padding = reduced.maxByOrNull { (old, _) -> old.length }?.first?.length!!
            val preview = reduced.map { (old, new) -> "${old.padEnd(padding)} => $new" }

            // View dimensions (rows, columns, insets)
            val cols = preview.maxByOrNull { entry -> entry.length }?.length!!
            val rows = preview.size.coerceAtMost(TextArea.DEFAULT_PREF_ROW_COUNT)
            val inset = 10.0

            // Construct an optimal viewport for the preview
            val text = TextArea(preview.joinToString("\n"))
            HBox.setHgrow(text, Priority.ALWAYS)
            text.font = Font.font("Monospaced")
            text.prefColumnCount = cols
            text.prefRowCount = rows
            text.isEditable = false
            text.isWrapText = false

            val box = HBox(text)
            box.padding = Insets(inset, inset, 0.0, inset)
            dialogPane.content = box
        }
    }
}

class ResultAlert(count: Int) : Alert(AlertType.INFORMATION) {
    init {
        title = "Result"
        headerText = "Rename complete."
        contentText = if (count > 0) {
            "$count file(s) renamed."
        } else {
            "Nothing was renamed."
        }
    }
}
