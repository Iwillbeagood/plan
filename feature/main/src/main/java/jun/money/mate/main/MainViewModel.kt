package jun.money.mate.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _mainDialogEffect = MutableStateFlow<MainDialogEffect>(MainDialogEffect.Idle)
    val mainDialogEffect: StateFlow<MainDialogEffect> get() = _mainDialogEffect

    fun onShowAppCloseDialog() {
        _mainDialogEffect.update { MainDialogEffect.AppClose }
    }

    fun onDialogDismiss() {
        _mainDialogEffect.update { MainDialogEffect.Idle }
    }
}

@Stable
sealed interface MainDialogEffect {
    @Immutable
    data object Idle : MainDialogEffect

    @Immutable
    data object AppClose : MainDialogEffect
}
