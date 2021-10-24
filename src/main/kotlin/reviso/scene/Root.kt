package reviso.scene

import java.io.File
import java.io.IOException as IoException
import java.net.URL as Url
import java.util.ResourceBundle
import java.util.regex.Pattern
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
import kotlin.collections.ArrayList
import reviso.Constants
import reviso.Preview

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
        if (event.code == KeyCode.ENTER){
            onOpen()
        }
    }

    @Fxml
    fun onPreviewSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                PreviewAlert(search()).showAndWait()
            }
        }
    }

    @Fxml
    fun onExecuteSearch() {
        if (fxSearch.text.isNotEmpty()) {
            onAction {
                val pairs = search()
                for ((source, target) in pairs) {
                    source.renameTo(target)
                }
                ResultAlert(pairs.size).showAndWait()
            }
        }
    }

    @Fxml
    fun onPreviewStandard() {
        onAction {
            PreviewAlert(standard()).showAndWait()
        }
    }

    @Fxml
    fun onExecuteStandard() {
        onAction {
            val pairs = standard()
            for ((source, target) in pairs) {
                source.renameTo(target)
            }
            ResultAlert(pairs.size).showAndWait()
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

    @Throws(NoDirectoryException::class)
    private fun fileTree(recursive: Boolean): Sequence<File> {
        if (directory != null) {
            // Collect the file tree as a sequence
            val file = File(directory!!)
            val tree = when(recursive) {
                true -> file.walk()
                false -> file.walk().maxDepth(1)
            }
            return tree.filter { node -> node.isFile }
        }
        throw NoDirectoryException()
    }

    @Throws(NoDirectoryException::class)
    private fun search(): List<Pair<File, File>> {
        val pairs = ArrayList<Pair<File, File>>()
        val search = fxSearch.text
        val replace = fxReplace.text
        val pattern = when(fxRegexSearch.isSelected) {
            true -> Pattern.compile(search)
            false -> null
        }

        // Collect a preview of each renamed file
        for (source in fileTree(fxRecursiveSearch.isSelected)) {
            val target = try {
                when(fxRegexSearch.isSelected) {
                    true -> Preview.regex(source, pattern!!, replace)
                    false -> Preview.simple(source, search, replace)
                }
            } catch (_: IllegalArgumentException) {
                continue
            }
            if (source.name != target.name) {
                // Ignore unchanged file names
                pairs.add(Pair(source, target))
            }
        }
        return pairs
    }

    @Throws(NoDirectoryException::class)
    private fun standard(): List<Pair<File, File>> {
        val pairs = ArrayList<Pair<File, File>>()

        // Collect a preview of each renamed file
        for (source in fileTree(fxRecursiveStandard.isSelected)) {
            val target = try {
                when (fxChoices.selectionModel.selectedItem) {
                    Constants.CHOICE_LOWER -> Preview.lower(source)
                    Constants.CHOICE_UPPER -> Preview.upper(source)
                    Constants.CHOICE_SENTENCE -> Preview.sentence(source)
                    Constants.CHOICE_TITLE_AP -> Preview.titleAp(source)
                    Constants.CHOICE_TITLE_SIMPLE -> Preview.titleSimple(source)
                    else -> throw RuntimeException() // Unreachable
                }
            } catch (_: IllegalArgumentException) {
                continue
            }
            if (source.name != target.name) {
                // Ignore unchanged file names
                pairs.add(Pair(source, target))
            }
        }
        return pairs
    }
}
