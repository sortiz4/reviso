package reviso.scene.alerts

import javafx.scene.control.Alert as BaseAlert
import javafx.scene.control.ButtonType
import javafx.stage.Stage
import reviso.Constants
import reviso.Resource

open class Alert(type: AlertType) : BaseAlert(type) {
    init {
        val stage = dialogPane.scene.window as Stage
        stage.icons.addAll(Resource.png(Constants.ICON_SET))
        buttonTypes[0] = ButtonType("Ok")
    }
}
