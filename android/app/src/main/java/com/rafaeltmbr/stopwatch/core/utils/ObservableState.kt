package com.rafaeltmbr.stopwatch.core.utils

interface StateObserver<T> {
    fun onStateChange(state: T)
}

class ObservableState<T>(private var _state: T) {
    private val stateObservers: MutableSet<StateObserver<T>> = mutableSetOf()

    val state: T
        get() = this._state

    fun addObserver(stateObserver: StateObserver<T>) {
        stateObservers.add(stateObserver)
    }

    fun removeObserver(stateObserver: StateObserver<T>) {
        stateObservers.remove(stateObserver)
    }

    fun updateState(state: T) {
        this._state = state

        for (observer in stateObservers) {
            observer.onStateChange(this._state)
        }
    }
}