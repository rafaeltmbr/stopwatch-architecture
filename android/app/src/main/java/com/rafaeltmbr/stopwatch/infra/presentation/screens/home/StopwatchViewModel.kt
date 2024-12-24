package com.rafaeltmbr.stopwatch.infra.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.rafaeltmbr.stopwatch.core.services.Stopwatch
import com.rafaeltmbr.stopwatch.core.services.StopwatchState
import com.rafaeltmbr.stopwatch.core.services.StopwatchStatus
import com.rafaeltmbr.stopwatch.core.utils.ObservableState
import com.rafaeltmbr.stopwatch.core.utils.StateObserver

data class ViewData(
    val timeFormatted: String,
    val status: StopwatchStatus
)

class StopwatchViewModel : ViewModel(), StateObserver<StopwatchState> {
    private val stopwatch = Stopwatch()

    private val _state: ObservableState<ViewData> = ObservableState(
        ViewData(
            timeFormatted = "%.1f s".format(stopwatch.state.state.seconds),
            status = stopwatch.state.state.status
        )
    )

    val state: ObservableState<ViewData>
        get() = this._state

    init {
        stopwatch.state.addObserver(this)
    }

    fun startOrResume() {
        when (_state.state.status) {
            StopwatchStatus.INITIAL -> stopwatch.start()
            StopwatchStatus.PAUSED -> stopwatch.resume()
            else -> {}
        }
    }

    fun pause() {
        stopwatch.pause()
    }

    fun reset() {
        stopwatch.reset()
    }

    override fun onStateChange(state: StopwatchState) {
        _state.updateState(
            ViewData(
                timeFormatted = "%.1f s".format(state.seconds),
                status = state.status
            )
        )
    }
}