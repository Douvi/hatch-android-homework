package co.hatch

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.hatch.theme.MyApplicationTestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MyScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loadingState by viewModel.loadingState.collectAsState()
    val devicesList by viewModel.deviceList.collectAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = loadingState.title
        )
        Divider(
            color = MaterialTheme.colors.secondary,
            thickness = 2.dp
        )
        if (devicesList.isEmpty()) {
            Text(
                text = "No Items",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.h6
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = devicesList, key = { it.id }) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable {
                                coroutineScope.launch(Dispatchers.Main) {
                                    context.startActivity(
                                        DeviceActivity.newInstance(
                                            context = context,
                                            deviceId = item.id
                                        )
                                    )
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .width(200.dp)
                                .padding(start = 22.dp),
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = item.rssi.toString(),
                            modifier = Modifier
                                .width(100.dp)
                                .padding(start = 22.dp),
                            style = MaterialTheme.typography.h6
                        )
                    }
                    Divider(
                        color = MaterialTheme.colors.secondary,
                        thickness = 0.2.dp
                    )
                }
            }
        }

    }
}