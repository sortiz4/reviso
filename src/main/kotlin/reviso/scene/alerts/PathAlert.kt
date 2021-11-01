package reviso.scene.alerts

open class PathAlert(message: String) : Alert(AlertType.ERROR) {
    init {
        title = "Error"
        headerText = message
        contentText = "Please open a location to begin."
    }
}
