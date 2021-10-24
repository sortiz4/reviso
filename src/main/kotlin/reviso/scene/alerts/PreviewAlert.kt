package reviso.scene.alerts

import java.io.File
import javafx.geometry.Insets
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font

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
