package co.hatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import co.hatch.models.HDevice
import co.hatch.service.ConnectedDevicesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceViewModelFactory(private val deviceId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeviceViewModel(deviceId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DeviceViewModel(
    private val deviceId: String = "0"
) : ViewModel() {

    private val _device = MutableStateFlow<HDevice>(HDevice.default())
    val device = _device.asStateFlow()

    init {
        loadingDevice()
    }

    private fun loadingDevice() {
        viewModelScope.launch(Dispatchers.IO) {
            ConnectedDevicesService.connectToDeviceBy(deviceId) {
                viewModelScope.launch(Dispatchers.IO) {
                    _device.emit(it)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            ConnectedDevicesService.disconnectToDevice(deviceId)
        }
    }

}