package jun.money.mate.designsystem_date.datetimepicker.models

import jun.money.mate.designsystem_date.datetimepicker.core.util.BaseConstants
import jun.money.mate.designsystem_date.datetimepicker.views.SelectionButton


abstract class BaseSelection {
    open val withButtonView: Boolean = true
    open val extraButton: SelectionButton? = null
    open val onExtraButtonClick: (() -> Unit)? = null
    open val negativeButton: SelectionButton? = BaseConstants.DEFAULT_NEGATIVE_BUTTON
    open val onNegativeClick: (() -> Unit)? = null
    open val positiveButton: SelectionButton = BaseConstants.DEFAULT_POSITIVE_BUTTON
}