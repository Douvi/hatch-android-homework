package co.hatch.service

import co.hatch.deviceClientLib.connectivity.ConnectivityClient
import co.hatch.deviceClientLib.model.Device
import co.hatch.models.HDevice
import co.hatch.models.toHDevice
import java.util.concurrent.atomic.AtomicReference

object ConnectedDevicesService {
    private var deviceConnectedTo: AtomicReference<String?> = AtomicReference(null)
    private val connectivityClient = ConnectivityClient.Factory.create()

    fun getDevices(): List<HDevice> =
        connectivityClient.discoverDevices().map {
            it.toHDevice()
        }.sortedBy { it.rssi }

    fun connectToDeviceBy(deviceId: String, deviceUpdate: (HDevice) -> Unit) {
        deviceConnectedTo.getAndUpdate { oldDeviceId ->
            if (oldDeviceId != null)
                disconnectToDevice(oldDeviceId)
            deviceId
        }
        connectivityClient.connectToDeviceBy(
            deviceId = deviceId,
            onDeviceStateChangeListener = object : ConnectivityClient.OnDeviceStateChangeListener {
                override fun onDeviceStateChanged(deviceId: String, device: Device) {
                    deviceUpdate(device.toHDevice())
                }
            }
        )
    }

    fun disconnectToDevice(deviceId: String) = connectivityClient.disconnectFromDevice(deviceId)





}