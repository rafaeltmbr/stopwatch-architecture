package com.rafaeltmbr.stopwatch.core.services

import com.rafaeltmbr.stopwatch.core.utils.ObservableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class StopwatchStatus {
    INITIAL,
    RUNNING,
    PAUSED
}

data class StopwatchState(
    val seconds: Float = 0f,
    val status: StopwatchStatus = StopwatchStatus.INITIAL
)

class Stopwatch {
    private var _state: ObservableState<StopwatchState> = ObservableState(StopwatchState())

    val state: ObservableState<StopwatchState>
        get() = this._state

    private var job: Job? = null

    fun start() {
        if (_state.state.status != StopwatchStatus.INITIAL) return

        loop()
    }

    fun resume() {
        if (_state.state.status != StopwatchStatus.PAUSED) return

        loop()
    }

    fun pause() {
        if (_state.state.status != StopwatchStatus.RUNNING) return

        job?.cancel()
        job = null
        _state.updateState(_state.state.copy(status = StopwatchStatus.PAUSED))
    }

    fun reset() {
        if (_state.state.status != StopwatchStatus.PAUSED) return

        _state.updateState(StopwatchState())
    }

    private fun loop() {
        _state.updateState(_state.state.copy(status = StopwatchStatus.RUNNING))

        job = CoroutineScope(Dispatchers.Default).launch {
            while (_state.state.status == StopwatchStatus.RUNNING) {
                _state.updateState(
                    _state.state.copy(seconds = _state.state.seconds + 0.1f)
                )

                delay(100L)
            }
        }
    }
}