package reviso.scene.alerts

class ResultAlert(count: Int) : Alert(AlertType.INFORMATION) {
    init {
        title = "Result"
        headerText = "Rename complete."
        contentText = if (count > 0) {
            "$count file(s) renamed."
        } else {
            "Nothing was renamed."
        }
    }
}
