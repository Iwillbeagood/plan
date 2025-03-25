package jun.money.mate.cost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jun.money.mate.cost.contract.CostEffect
import jun.money.mate.cost.contract.CostDetailState
import jun.money.mate.data_api.database.ChallengeRepository
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
internal class CostDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id = savedStateHandle.toRoute<Route.Challenge.Detail>().id

    val costDetailState: StateFlow<CostDetailState> = challengeRepository.getChallengeById(id)
        .map {
            CostDetailState.CostDetailData(it)
        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CostDetailState.Loading
    )

    private val _costEffect = MutableSharedFlow<CostEffect>()
    val costEffect: SharedFlow<CostEffect> get() = _costEffect.asSharedFlow()

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
            _costEffect.emit(CostEffect.ShowSnackBar(messageType))
        }
    }
}