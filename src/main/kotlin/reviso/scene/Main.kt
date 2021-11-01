package reviso.scene

import java.io.File
import java.net.URL as Url
import java.util.ResourceBundle
import javafx.fxml.FXML as Fxml
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.DragEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.TransferMode
import javafx.stage.DirectoryChooser
import reviso.Case
import reviso.Reviso
import reviso.scene.alerts.InvalidPathAlert
import reviso.scene.alerts.NullPathAlert
import reviso.scene.alerts.PreviewAlert
import reviso.scene.alerts.ResultAlert

class Main : Initializable {
    // JavaFx location properties
    @Fxml
    private lateinit var fxPath: TextField
    @Fxml
    private lateinit var fxStatus: Label

    // JavaFx common cases properties
    @Fxml
    private lateinit var fxChoices: ComboBox<String>
    @Fxml
    private lateinit var fxExtensionCase: CheckBox
    @Fxml
    private lateinit var fxRecursiveCase: CheckBox

    // JavaFx search and replace properties
    @Fxml
    private lateinit var fxSearch: TextField
    @Fxml
    private lateinit var fxReplace: TextField
    @Fxml
    private lateinit var fxExpressionSearch: CheckBox
    @Fxml
    private lateinit var fxExtensionSearch: CheckBox
    @Fxml
    private lateinit var fxRecursiveSearch: CheckBox

    // Regular properties
    private var path: File? = null

    @Fxml
    fun onBrowse() {
        val window = DirectoryChooser()

        path?.let {
            // Start from the current path
            window.initialDirectory = it
        }

        window.showDialog(fxPath.scene.window)?.let {
            // Update the path field with the selected directory
            fxPath.text = it.path
        }
    }

    @Fxml
    fun onOpen() {
        File(fxPath.text.trim()).also {
            if (it.path.isNotEmpty()) {
                if (it.isDirectory) {
                    // Set the path and status when the path is a directory
                    path = it
                    fxStatus.text = "Open: ${it.path}"
                } else {
                    // Alert the user when the path is not a directory
                    InvalidPathAlert(it.path).showAndWait()
                }
            }
        }
    }

    @Fxml
    fun onDragOver(event: DragEvent) {
        if (event.dragboard.hasFiles()) {
            // Indicate this view supports file dragging
            event.acceptTransferModes(TransferMode.COPY)
        }
    }

    @Fxml
    fun onDragDrop(event: DragEvent) {
        event.dragboard.files?.getOrNull(0)?.let {
            fxPath.text = if (it.isDirectory) {
                // Set the path to the directory
                it.path
            } else {
                // Infer the path from the file
                it.parent
            }
        }
    }

    @Fxml
    fun onKeyRelease(event: KeyEvent) {
        if (event.code == KeyCode.ENTER) {
            onOpen()
        }
    }

    @Fxml
    fun onPreviewCase() {
        onAction {
            PreviewAlert(onCase().preview(relative = false)).showAndWait()
        }
    }

    @Fxml
    fun onExecuteCase() {
        onAction {
            ResultAlert(onCase().rename()).showAndWait()
        }
    }

    @Fxml
    fun onPreviewSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                PreviewAlert(onSearch().preview(relative = false)).showAndWait()
            }
        }
    }

    @Fxml
    fun onExecuteSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                ResultAlert(onSearch().rename()).showAndWait()
            }
        }
    }

    override fun initialize(resource: Url?, bundle: ResourceBundle?) {
        // Populate the list of choices
        fxChoices.items.addAll(*Case.guiNames())
        fxChoices.selectionModel.selectFirst()
    }

    private fun onAction(action: () -> Unit) {
        try {
            action()
        } catch (_: NullPointerException) {
            NullPathAlert().showAndWait()
        }
    }

    private fun onCase(): Reviso {
        return (
            Reviso()
                .paths(path!!)
                .case(fxChoices.selectionModel.selectedItem)
                .isRecursive(fxRecursiveCase.isSelected)
                .withExtension(fxExtensionCase.isSelected)
        )
    }

    private fun onSearch(): Reviso {
        return (
            Reviso()
                .paths(path!!)
                .search(fxSearch.text)
                .replace(fxReplace.text)
                .isRecursive(fxRecursiveSearch.isSelected)
                .isExpression(fxExpressionSearch.isSelected)
                .withExtension(fxExtensionSearch.isSelected)
        )
    }
}
