package reviso.scene.alerts

import javafx.scene.control.Alert as BaseAlert
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import reviso.Resources

open class Alert(type: AlertType) : BaseAlert(type) {
    init {
        val stage = dialogPane.scene.window as Stage
        stage.icons.addAll(Resources.icons())
        buttonTypes[0] = ButtonType("Ok")
    }
}
