package reviso.scene

import java.io.File
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
import reviso.Rename
import reviso.Transform

class Root : Initializable {
    // JavaFx path properties
    @Fxml
    private lateinit var fxPath: TextField
    @Fxml
    private lateinit var fxStatus: Label

    // JavaFx pattern properties
    @Fxml
    private lateinit var fxSearch: TextField
    @Fxml
    private lateinit var fxReplace: TextField
    @Fxml
    private lateinit var fxRegexSearch: CheckBox
    @Fxml
    private lateinit var fxRecursiveSearch: CheckBox

    // JavaFx standard properties
    @Fxml
    private lateinit var fxChoices: ComboBox<String>
    @Fxml
    private lateinit var fxRecursiveStandard: CheckBox

    // Regular properties
    private var directory: String? = null

    @Fxml
    fun onBrowse(event: ActionEvent) {
        val window = DirectoryChooser()
        if(this.directory != null) {
            window.initialDirectory = File(this.directory)
        }
        val result = window.showDialog((event.target as Node).scene.window)
        if(result != null) {
            this.fxPath.text = result.path
        }
    }

    @Fxml
    fun onOpen() {
        // Trim the input and ignore empty requests
        val path = this.fxPath.text.trim()
        if(!path.isEmpty()) {
            val file = File(path)
            if(file.isDirectory) {
                // Set the directory and status when the path is a directory
                this.directory = this.fxPath.text
                this.fxStatus.text = Constants.open(this.fxPath.text)
            } else {
                // Alert the user when the path is not a directory
                InvalidPathAlert(file.path).showAndWait()
            }
        }
    }

    @Fxml
    fun onDragOver(event: DragEvent) {
        if(event.dragboard.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY)
        }
    }

    @Fxml
    fun onDragDrop(event: DragEvent) {
        var set = false
        val selection = event.dragboard
        if(selection.hasFiles()) {
            for(file in selection.files) {
                if(file.isDirectory) {
                    // Set the path to the first directory
                    this.fxPath.text = file.path
                    set = true
                    break
                }
            }
            if(!set && selection.files.size > 0) {
                // Infer the path from the first file
                this.fxPath.text = selection.files[0].parent
            }
        }
    }

    @Fxml
    fun onKeyRelease(event: KeyEvent) {
        if(event.code == KeyCode.ENTER){
            this.onOpen()
        }
    }

    @Fxml
    fun onPreviewSearch() {
        if(!this.fxSearch.text.isEmpty()) {
            val pairs = ArrayList<Pair<File, File>>()
            val search = this.fxSearch.text
            val replace = this.fxReplace.text
            val pattern = when(this.fxRegexSearch.isSelected) {
                true -> Pattern.compile(search)
                false -> null
            }
            for(node in this.fileTree(this.fxRecursiveSearch.isSelected)) {
                when(this.fxRegexSearch.isSelected) {
                    true -> pairs.add(Pair(node, Transform.regex(node, pattern!!, replace)))
                    false -> pairs.add(Pair(node, Transform.simple(node, search, replace)))
                }
            }
            if(pairs.size > 0) {
                PreviewAlert(pairs).showAndWait()
            }
        }
    }

    @Fxml
    fun onExecuteSearch() {
        if(!this.fxSearch.text.isEmpty()) {
            val search = this.fxSearch.text
            val replace = this.fxReplace.text
            val pattern = when(this.fxRegexSearch.isSelected) {
                true -> Pattern.compile(search)
                false -> null
            }
            for(node in this.fileTree(this.fxRecursiveSearch.isSelected)) {
                when(this.fxRegexSearch.isSelected) {
                    true -> Rename.regex(node, pattern!!, replace)
                    false -> Rename.simple(node, search, replace)
                }
            }
        }
    }

    @Fxml
    fun onPreviewStandard() {
        val pairs = ArrayList<Pair<File, File>>()
        for(node in this.fileTree(this.fxRecursiveStandard.isSelected)) {
            when(this.fxChoices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> pairs.add(Pair(node, Transform.lower(node)))
                Constants.CHOICE_UPPER -> pairs.add(Pair(node, Transform.upper(node)))
                Constants.CHOICE_SENTENCE -> pairs.add(Pair(node, Transform.sentence(node)))
                Constants.CHOICE_TITLE_AP -> pairs.add(Pair(node, Transform.titleAp(node)))
                Constants.CHOICE_TITLE_SIMPLE -> pairs.add(Pair(node, Transform.titleSimple(node)))
                else -> throw RuntimeException() // Unreachable
            }
        }
        if(pairs.size > 0) {
            PreviewAlert(pairs).showAndWait()
        }
    }

    @Fxml
    fun onExecuteStandard() {
        for(node in this.fileTree(this.fxRecursiveStandard.isSelected)) {
            when(this.fxChoices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> Rename.lower(node)
                Constants.CHOICE_UPPER -> Rename.upper(node)
                Constants.CHOICE_SENTENCE -> Rename.sentence(node)
                Constants.CHOICE_TITLE_AP -> Rename.titleAp(node)
                Constants.CHOICE_TITLE_SIMPLE -> Rename.titleSimple(node)
                else -> throw RuntimeException() // Unreachable
            }
        }
    }

    override fun initialize(resource: Url?, bundle: ResourceBundle?) {
        this.fxChoices.items.addAll(*Constants.CHOICES)
        this.fxChoices.selectionModel.selectFirst()
    }

    private fun fileTree(recursive: Boolean): Sequence<File> {
        if(this.directory != null) {
            // Collect the file tree as a sequence
            val file = File(this.directory)
            val tree = when(recursive) {
                true -> file.walk()
                false -> file.walk().maxDepth(1)
            }
            return tree.filter { node -> node.isFile }
        } else {
            // Alert the user when no directory is found
            EmptyPathAlert().showAndWait()
        }
        return emptySequence()
    }
}
