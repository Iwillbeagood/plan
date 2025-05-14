package jun.money.mate.utils.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * StateFlow의 값을 특정 타입으로 캐스팅한 후, 해당 타입에 맞는 로직을 실행하는 함수입니다.
 * ```
 * fun showMessage() {
 *    uiState.withData<UiState.UiData> { uiData ->
 *        showToast(uiData.message)
 *    }
 * }
 * ```
 * @param T 처리할 상태의 타입
 * @param action 상태 타입에 대해 수행할 작업을 정의하는 람다 함수
 */
inline fun <reified T> StateFlow<*>.withData(action: (T) -> Unit) {
    val uiState = value
    if (uiState is T) {
        action(uiState)
    }
}

/**
 * MutableStateFlow의 상태 값을 특정 타입으로 캐스팅한 후, 상태를 변형하여 업데이트하는 함수입니다.
 * ```
 * fun updateMessage() {
 *   uiState.updateWithData<UiState, UiState.UiData> { uiData ->
 *       uiData.copy(message = "Hello, World!")
 *   }
 * }
 * ```
 * @param State 상태의 부모 타입
 * @param T 상태 타입의 자식 타입
 * @param action 상태를 변형하는 작업을 정의하는 람다 함수
 */
inline fun <State, reified T : State> MutableStateFlow<State>.updateWithData(action: (T) -> T) {
    update { state ->
        if (state is T) {
            action(state) // 상태가 T 타입이라면 변형한 후 반환
        } else {
            state // 상태가 T 타입이 아니라면 그대로 반환
        }
    }
}
