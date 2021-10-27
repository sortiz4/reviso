package reviso.scene

import java.io.File
import java.io.IOException as IoException
import java.net.URL as Url
import java.nio.file.Paths
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.FXML as Fxml
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.DragEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.TransferMode
import javafx.stage.DirectoryChooser
import reviso.Constants
import reviso.Reviso
import reviso.scene.alerts.InvalidDirectoryAlert
import reviso.scene.alerts.NoDirectoryAlert
import reviso.scene.alerts.PreviewAlert
import reviso.scene.alerts.ResultAlert

class Root : Initializable {
    private class NoDirectoryException : IoException()

    // JavaFx source directory properties
    @Fxml
    private lateinit var fxPath: TextField
    @Fxml
    private lateinit var fxStatus: Label

    // JavaFx pattern matching properties
    @Fxml
    private lateinit var fxSearch: TextField
    @Fxml
    private lateinit var fxReplace: TextField
    @Fxml
    private lateinit var fxRegexSearch: CheckBox
    @Fxml
    private lateinit var fxRecursiveSearch: CheckBox

    // JavaFx standard method properties
    @Fxml
    private lateinit var fxChoices: ComboBox<String>
    @Fxml
    private lateinit var fxRecursiveStandard: CheckBox

    // Regular properties
    private var directory: String? = null

    @Fxml
    fun onBrowse(event: ActionEvent) {
        val window = DirectoryChooser()
        if (directory != null) {
            // Start from the current directory
            window.initialDirectory = File(directory!!)
        }
        val result = window.showDialog((event.target as Node).scene.window)
        if (result != null) {
            // Update the path field with the selected directory
            fxPath.text = result.path
        }
    }

    @Fxml
    fun onOpen() {
        // Trim the input and ignore empty requests
        val path = fxPath.text.trim()
        if (path.isNotEmpty()) {
            val file = File(path)
            if (file.isDirectory) {
                // Set the directory and status when the path is a directory
                directory = fxPath.text
                fxStatus.text = Constants.open(fxPath.text)
            } else {
                // Alert the user when the path is not a directory
                InvalidDirectoryAlert(file.path).showAndWait()
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
        var didSet = false
        val selection = event.dragboard
        if (selection.hasFiles()) {
            for (file in selection.files) {
                if (file.isDirectory) {
                    // Set the path to the first directory
                    fxPath.text = file.path
                    didSet = true
                    break
                }
            }
            if (!didSet && selection.files.size > 0) {
                // Infer the path from the first file
                fxPath.text = selection.files[0].parent
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
    fun onPreviewSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                PreviewAlert(onStart().setRecursive(fxRecursiveSearch.isSelected).preview()).showAndWait()
            }
        }
    }

    @Fxml
    fun onExecuteSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                ResultAlert(onStart().setRecursive(fxRecursiveSearch.isSelected).rename()).showAndWait()
            }
        }
    }

    @Fxml
    fun onPreviewStandard() {
        onAction {
            PreviewAlert(onStart().setRecursive(fxRecursiveStandard.isSelected).preview()).showAndWait()
        }
    }

    @Fxml
    fun onExecuteStandard() {
        onAction {
            ResultAlert(onStart().setRecursive(fxRecursiveStandard.isSelected).rename()).showAndWait()
        }
    }

    override fun initialize(resource: Url?, bundle: ResourceBundle?) {
        // Populate the list of choices
        fxChoices.items.addAll(*Constants.CHOICES)
        fxChoices.selectionModel.selectFirst()
    }

    private fun onAction(action: () -> Unit) {
        try {
            action()
        } catch (_: NoDirectoryException) {
            NoDirectoryAlert().showAndWait()
        }
    }

    private fun onStart(): Reviso {
        return (
            Reviso()
                .setPaths(setOf(Paths.get(directory!!)))
                .setMode(fxChoices.selectionModel.selectedItem)
                .setSearch(fxSearch.text)
                .setReplace(fxReplace.text)
                .setRegex(fxRegexSearch.isSelected)
        )
    }
}
