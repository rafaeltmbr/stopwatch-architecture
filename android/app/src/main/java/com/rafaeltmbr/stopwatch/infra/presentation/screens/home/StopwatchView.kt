package com.rafaeltmbr.stopwatch.infra.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafaeltmbr.stopwatch.core.services.StopwatchStatus
import com.rafaeltmbr.stopwatch.core.utils.StateObserver
import com.rafaeltmbr.stopwatch.infra.presentation.theme.StopwatchTheme

@Composable
fun StopwatchView(modifier: Modifier = Modifier, vm: StopwatchViewModel = viewModel()) {
    var viewState by remember { mutableStateOf(vm.state.state) }

    DisposableEffect(Unit) {
        val stateObserver = object : StateObserver<ViewData> {
            override fun onStateChange(state: ViewData) {
                viewState = state
            }
        }

        vm.state.addObserver(stateObserver)

        onDispose {
            vm.state.removeObserver(stateObserver)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(viewState.timeFormatted, style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(16.dp))

        when (viewState.status) {
            StopwatchStatus.INITIAL -> {
                Button(onClick = vm::startOrResume) {
                    Text("Start")
                }
            }

            StopwatchStatus.PAUSED -> {
                Row {
                    Button(onClick = vm::startOrResume) {
                        Text("Resume")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = vm::reset, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Reset")
                    }
                }
            }

            StopwatchStatus.RUNNING -> {
                Button(onClick = vm::pause) {
                    Text("Pause")
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun StopwatchViewPreview() {
    StopwatchTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            StopwatchView(modifier = Modifier.padding(innerPadding))
        }
    }
}