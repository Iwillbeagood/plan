package jun.money.mate.challenge

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.challenge.contract.ChallengeEffect
import jun.money.mate.challenge.contract.ChallengeState
import jun.money.mate.dataApi.database.ChallengeRepository
import jun.money.mate.model.etc.error.MessageType
import jun.money.mate.navigation.Route
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val id = savedStateHandle.toRoute<Route.Challenge.Detail>().id

    val challengeState: StateFlow<ChallengeState> = challengeRepository.getChallengeById(id)
        .map {
            ChallengeState.ChallengeData(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChallengeState.Loading,
        )

    private val _challengeEffect = MutableSharedFlow<ChallengeEffect>()
    val challengeEffect: SharedFlow<ChallengeEffect> get() = _challengeEffect.asSharedFlow()

    fun changeAchieve(executed: Boolean, id: Long) {
        viewModelScope.launch {
            challengeRepository.updateChallengeAchieved(id, executed)
        }
    }

    fun giveUpChallenge() {
        viewModelScope.launch {
            challengeRepository.deleteById(id)
            showSnackBar(MessageType.Message("챌린지를 포기하였습니다"))
        }
    }

    private fun showSnackBar(messageType: MessageType) {
        viewModelScope.launch {
            _challengeEffect.emit(ChallengeEffect.ShowSnackBar(messageType))
        }
    }
}
