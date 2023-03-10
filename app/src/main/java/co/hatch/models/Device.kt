package co.hatch.models

import co.hatch.deviceClientLib.model.Device
import java.util.Date

data class HDevice(
    val connected: Boolean,
    val elapsedSecsConnected: Long,
    val id: String,
    val latestConnectedTime: Date?,
    val name: String,
    val rssi: Int
) {
  companion object {
      fun default() = HDevice(
          connected = false,
          elapsedSecsConnected = 0L,
          id = "",
          latestConnectedTime = Date(),
          name = "",
          rssi = 1
      )
  }
}

fun Device.toHDevice(): HDevice = HDevice(
    id = this.id,
    name = this.name,
    rssi = this.rssi,
    connected = this.connected,
    elapsedSecsConnected = this.elapsedSecsConnected,
    latestConnectedTime = this.latestConnectedTime
)
