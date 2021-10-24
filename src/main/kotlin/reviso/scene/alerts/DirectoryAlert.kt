package reviso.scene.alerts

open class DirectoryAlert(message: String) : Alert(AlertType.ERROR) {
    init {
        title = "Error"
        headerText = message
        contentText = "Please open a directory to begin."
    }
}
