package rename.scene

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
import rename.Constants
import rename.Message
import rename.Preview
import rename.Rename

class Main : Initializable {
    // Path properties
    @Fxml
    private lateinit var path: TextField
    @Fxml
    private lateinit var status: Label

    // Pattern properties
    @Fxml
    private lateinit var search: TextField
    @Fxml
    private lateinit var replace: TextField
    @Fxml
    private lateinit var regexSearch: CheckBox
    @Fxml
    private lateinit var recursiveSearch: CheckBox

    // Standard properties
    @Fxml
    private lateinit var choices: ComboBox<String>
    @Fxml
    private lateinit var recursiveStandard: CheckBox

    // Regular properties
    private var lastPath: String? = null

    @Fxml
    fun browse(event: ActionEvent) {
        val window = DirectoryChooser()
        if(this.lastPath != null) {
            window.initialDirectory = File(this.lastPath)
        }
        val result = window.showDialog((event.target as Node).scene.window)
        if(result != null) {
            this.path.text = result.path
        }
    }

    @Fxml
    fun open() {
        val path = this.path.text.trim()
        if(!path.isEmpty()) {
            val file = File(path)
            if(file.isDirectory) {
                this.lastPath = this.path.text
                this.status.text = Message.open(this.path.text)
            } else {
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
        val board = event.dragboard
        if(board.hasFiles()) {
            for(file in board.files) {
                if(file.isDirectory) {
                    this.path.text = file.path
                    set = true
                    break
                }
            }
            if(!set && board.files.size > 0) {
                this.path.text = board.files[0].parent
            }
        }
    }

    @Fxml
    fun openKey(event: KeyEvent) {
        if(event.code == KeyCode.ENTER){
            this.open()
        }
    }

    @Fxml
    fun previewSearch() {
        if(!this.search.text.isEmpty()) {
            val pairs = ArrayList<Pair<File, File>>()
            val pattern = when(this.regexSearch.isSelected) {
                true -> Pattern.compile(this.search.text)
                false -> null
            }
            for(node in this.getFileTree(this.recursiveSearch.isSelected)) {
                when(this.regexSearch.isSelected) {
                    true -> pairs.add(Pair(node, Preview.regex(node, pattern!!, this.replace.text)))
                    false -> pairs.add(Pair(node, Preview.simple(node, this.search.text, this.replace.text)))
                }
            }
            if(pairs.size > 0) {
                PreviewAlert(pairs).showAndWait()
            }
        }
    }

    @Fxml
    fun executeSearch() {
        if(!this.search.text.isEmpty()) {
            val pattern = when(this.regexSearch.isSelected) {
                true -> Pattern.compile(this.search.text)
                false -> null
            }
            for(node in this.getFileTree(this.recursiveSearch.isSelected)) {
                when(this.regexSearch.isSelected) {
                    true -> Rename.regex(node, pattern!!, this.replace.text)
                    false -> Rename.simple(node, this.search.text, this.replace.text)
                }
            }
        }
    }

    @Fxml
    fun previewStandard() {
        val pairs = ArrayList<Pair<File, File>>()
        for(node in this.getFileTree(this.recursiveStandard.isSelected)) {
            when(this.choices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> pairs.add(Pair(node, Preview.lower(node)))
                Constants.CHOICE_UPPER -> pairs.add(Pair(node, Preview.upper(node)))
                Constants.CHOICE_SENTENCE -> pairs.add(Pair(node, Preview.sentence(node)))
                Constants.CHOICE_TITLE_SIMPLE -> pairs.add(Pair(node, Preview.titleSimple(node)))
                else -> throw RuntimeException()
            }
        }
        if(pairs.size > 0) {
            PreviewAlert(pairs).showAndWait()
        }
    }

    @Fxml
    fun executeStandard() {
        for(node in this.getFileTree(this.recursiveStandard.isSelected)) {
            when(this.choices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> Rename.lower(node)
                Constants.CHOICE_UPPER -> Rename.upper(node)
                Constants.CHOICE_SENTENCE -> Rename.sentence(node)
                Constants.CHOICE_TITLE_SIMPLE -> Rename.titleSimple(node)
                else -> throw RuntimeException()
            }
        }
    }

    private fun getFileTree(recursive: Boolean): Sequence<File> {
        if(this.lastPath != null) {
            val file = File(this.lastPath)
            val tree = when(recursive) {
                true -> file.walk()
                false -> file.walk().maxDepth(1)
            }
            return tree.filter { node -> node.isFile }
        } else {
            EmptyPathAlert().showAndWait()
        }
        return emptySequence()
    }

    override fun initialize(resource: Url?, bundle: ResourceBundle?) {
        this.choices.items.addAll(*Constants.CHOICES)
        this.choices.selectionModel.selectFirst()
    }
}
