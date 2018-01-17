package rename.scene

import java.io.File
import java.net.URL as Url
import java.util.ResourceBundle
import javafx.fxml.FXML as Fxml
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import kotlin.collections.ArrayList
import rename.Constants
import rename.Message
import rename.Rename
import rename.Standard

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
    fun previewSearch() {
        // search.text
        // replace.text
        // regexSearch.isSelected
        // recursiveSearch.isSelected
        val pairs = ArrayList<Pair<File, File>>()
        for(node in this.getFileTree(this.recursiveSearch.isSelected)) {
            // Preview based on search
            pairs.add(Pair(node, node))
        }
        if(pairs.size > 0) {
            Preview(pairs).showAndWait()
        }
    }

    @Fxml
    fun executeSearch() {
        // search.text
        // replace.text
        // regexSearch.isSelected
        // recursiveSearch.isSelected
        for(node in this.getFileTree(this.recursiveSearch.isSelected)) {
            // Rename based on search
        }
    }

    @Fxml
    fun previewStandard() {
        val pairs = ArrayList<Pair<File, File>>()
        for(node in this.getFileTree(this.recursiveStandard.isSelected)) {
            // TODO: Optimize mode comparison
            when(this.choices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> pairs.add(Pair(node, Standard.lower(node)))
                Constants.CHOICE_UPPER -> pairs.add(Pair(node, Standard.upper(node)))
                else -> throw RuntimeException()
            }
        }
        if(pairs.size > 0) {
            Preview(pairs).showAndWait()
        }
    }

    @Fxml
    fun executeStandard() {
        for(node in this.getFileTree(this.recursiveStandard.isSelected)) {
            // TODO: Optimize mode comparison
            when(this.choices.selectionModel.selectedItem) {
                Constants.CHOICE_LOWER -> Rename.lower(node)
                Constants.CHOICE_UPPER -> Rename.upper(node)
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
