package co.hatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.hatch.models.HDevice
import co.hatch.service.ConnectedDevicesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class State(val title: String) {
    object Loading: State("Loading...")
    data class Loaded(val counter: Int): State("Loaded (in $counter)")
    data class LoadingFail(val counter: Int, val error: Throwable): State("Loading Fail (in $counter)- ${error.message}")
}

data class Devices(
    val devices: List<HDevice>
)

class MainViewModel: ViewModel() {

    private val _loadingState = MutableStateFlow<State>(State.Loaded(0))
    val loadingState = _loadingState.asStateFlow()

    private val _deviceList = MutableSharedFlow<List<HDevice>>()
    val deviceList = _deviceList.asSharedFlow()

    init {
        loadingDevices()
    }

    private fun loadingDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            var error: Throwable? = null
            while (isActive) {
                error = null
                _loadingState.update { State.Loading }
                try {
                    val list = ConnectedDevicesService.getDevices()
                    _loadingState.update { State.Loaded(10) }
                    _deviceList.emit(list)
                } catch (e: Throwable) {
                    error = e
                    _loadingState.emit(State.LoadingFail(10, e))
                }

                repeat(10) { counter ->
                    if (error != null) {
                        _loadingState.emit(State.LoadingFail(10, error))
                    } else {
                        _loadingState.update { State.Loaded(10 - counter) }
                    }

                    Thread.sleep(SLEEP_IN_MS)
                }
            }
        }
    }

    companion object {
        const val SLEEP_IN_MS = 1_000L
    }

}