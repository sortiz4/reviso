package rename

import java.io.File
import java.net.URL as Url
import java.util.ResourceBundle
import javafx.fxml.FXML as Fxml
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField

class MainController : Initializable {
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

    @Fxml
    fun open() {
        val path = this.path.text.trim()
        if(path.isEmpty()) {
            val alert = Alert(AlertType.ERROR)
            alert.headerText = "File system error"
            alert.contentText = "Please open a directory."
            alert.showAndWait()
        } else {
            val file = File(path)
            if(file.isDirectory) {
                this.status.text = "Open: " + this.path.text
            } else {
                val alert = Alert(AlertType.ERROR)
                alert.headerText = "File system error"
                alert.contentText = "\"${file.path}\" is not a directory."
                alert.showAndWait()
            }
        }
    }

    @Fxml
    fun previewSearch() {
        // search.text
        // replace.text
        // regexSearch.isSelected
        // recursiveSearch.isSelected
        //for(node in file.walk().maxDepth(1)) {
        //    if(node.isFile) {
        //        println(node.name)
        //    }
        //}
    }

    @Fxml
    fun executeSearch() {
        // search.text
        // replace.text
        // regexSearch.isSelected
        // recursiveSearch.isSelected
    }

    @Fxml
    fun previewStandard() {
        // recursiveStandard.isSelected
    }

    @Fxml
    fun executeStandard() {
        // recursiveStandard.isSelected
    }

    override fun initialize(resource: Url?, bundle: ResourceBundle?) {
        this.choices.items.addAll(*Constants.CHOICES)
        this.choices.selectionModel.selectFirst()
    }
}
