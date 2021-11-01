package reviso.scene.alerts

import javafx.geometry.Insets
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Font

class PreviewAlert(preview: Collection<String>) : Alert(AlertType.CONFIRMATION) {
    init {
        title = "Preview"
        headerText = "Preview the changes."
        buttonTypes.remove(1, buttonTypes.size)
        if (preview.isEmpty()) {
            contentText = "No changes detected."
        } else {
            isResizable = true

            // View dimensions (rows, columns, insets)
            val cols = preview.maxByOrNull { it.length }?.length!!
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
