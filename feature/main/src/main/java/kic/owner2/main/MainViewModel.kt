package kic.owner2.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kic.owner2.domain.AppVersionSettingUsecase
import kic.owner2.domain.login.CheckLoginRequirementUseCase
import jun.money.mate.model.error.MessageType
import kic.owner2.utils.etc.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appVersionSettingUsecase: AppVersionSettingUsecase,
    private val checkLoginRequirementUseCase: CheckLoginRequirementUseCase
) : ViewModel() {

    private val _mainDialogEffect = MutableStateFlow<MainDialogEffect>(MainDialogEffect.Idle)
    val mainDialogEffect: StateFlow<MainDialogEffect> get() = _mainDialogEffect

    fun initViewModel(currentVersionName: String) {
        viewModelScope.launch {
            appVersionSettingUsecase(
                currentVersionName = currentVersionName,
                onUpdateOptional = { url, message ->
                    _mainDialogEffect.update {
                        MainDialogEffect.UpdateOptional(url, message)
                    }
                },
                onUpdateForced = { url, message ->
                    _mainDialogEffect.update {
                        MainDialogEffect.UpdateForced(url, message)
                    }
                }
            )
        }
    }

    fun onDismissUpdateDialog() {
        _mainDialogEffect.update { MainDialogEffect.Idle }
    }

    fun onShowAppCloseDialog() {
        _mainDialogEffect.update { MainDialogEffect.AppClose }
    }

    fun isLoginRequired(): Boolean = runBlocking {
        checkLoginRequirementUseCase {
            Logger.e("error : $it")
        }
    }
}

@Stable
sealed interface MainDialogEffect {
    @Immutable
    data object Idle : MainDialogEffect

    @Immutable
    data class UpdateOptional(val url: String, val message: String) : MainDialogEffect

    @Immutable
    data class UpdateForced(val url: String, val message: String) : MainDialogEffect


    @Immutable
    data object AppClose : MainDialogEffect
}