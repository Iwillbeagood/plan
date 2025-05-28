package jun.money.mate.designsystemDate.datetimepicker.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.Serializable

abstract class BaseTypeState : Serializable {

    open var inputDisabled by mutableStateOf(false)

    /**
     * Disables the input for the use-case view state.
     */
    fun disableInput() {
        inputDisabled = true
    }

    abstract fun reset()
}
