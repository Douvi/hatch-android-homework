package co.hatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.hatch.theme.MyApplicationTestTheme

class DeviceActivity: AppCompatActivity() {
    private val viewModel: DeviceViewModel by viewModels {
        DeviceViewModelFactory(intent.getStringExtra(KEY_DEVICE) ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyDevice(viewModel)
                }
            }
        }
    }

    companion object {
        private const val KEY_DEVICE = "KEY_DEVICE"
        fun newInstance(context: Context, deviceId: String): Intent =
            Intent(context, DeviceActivity::class.java).apply {
                putExtra(KEY_DEVICE, deviceId)
            }
    }
}

@Composable
fun MyDevice(viewModel: DeviceViewModel) {
    val device by viewModel.device.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.id
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.name
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.rssi.toString()
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.elapsedSecsConnected.toString()
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.latestConnectedTime.toString()
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        Text(
            modifier = Modifier.padding(10.dp),
            text = device.connected.toString()
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
    }
}